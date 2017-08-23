#!/usr/bin/python

import json
import requests

URL = 'http://localhost:5060'
API_URL = URL + '/api/v1'
ACC_URL = API_URL + '/account'
TRANS_URL = API_URL + '/transaction'

def is_close_enough(n1, n2):
    return abs(n1 - n2) <= 0.01


wallets = dict()

# accounts
print "\n--- Account creation --- "
with open("users.json", 'r') as users_file:
    users_map = json.load(users_file)

users_list = []
for user in users_map:
    name = user["name"]
    balance = user["balance"]
    response = requests.post(ACC_URL, json = {"name": name, "balance": balance})
    answer = response.json()

    if name == "invalid" or name == "":
        # special accounts for tests that expected to fail
        assert answer["success"] == False, "Invalid account was created! " + answer["message"]
    else:
        assert answer["success"] == True, "Can't create account: " + answer["message"]
        id = answer["account"]["id"]
        users_list.append(id)
        wallets[id] = float(balance)
        print "[id = %d] account created for %s with balance: %s" % (id, name, balance)

# Transfer money
print "\n--- Transactions ---"
with open("tr.json", 'r') as tr_file:
    tr_map = json.load(tr_file)

passed = dict()
for tr in tr_map:
    n = tr["n"]
    snd = tr["from"]
    rcv = tr["to"]
    amn = tr["amount"]
    famn = float(amn)
    response = requests.post(TRANS_URL, json = {"from": snd, "to": rcv, "amount": amn})
    answer = response.json()

    if answer["message"] == "Transfer failed: not enough money.":
        # check that it is a correct exception
        account = requests.get(ACC_URL + "/" + snd).json()
        assert float(account["account"]["balance"]) - famn < 0, "Invalid 'Not enough money' exception: " + account
    else:
        assert answer["success"] == True, "Can't process transaction: " + answer["message"]
        print "[%s] transaction passed from %s to %s with amount %s " % (n, snd, rcv, amn)
        # count all related to account transaction
        i = int(snd)
        j = int(rcv)
        passed[i] = passed.get(i, 0) + 1
        passed[j] = passed.get(j, 0) + 1
        wallets[i] = wallets[i] - famn
        wallets[j] = wallets[j] + famn

# check correctness
for u in users_list:
    tr_for_account_url = ACC_URL + ("/%d/transactions" % u)
    res = requests.get(tr_for_account_url).json()
    account = requests.get(ACC_URL + "/" + str(u)).json()
    assert res["success"] == True, "Can't get transactions list for account %d: %s" % (u, res["message"])
    assert len(res["transactions"]) == passed[u], "Transactions list isn't complete for user %d!" %u
    b = account["account"]["balance"]
    assert is_close_enough(b, wallets[u]) == True, "Balance inconsistency for account %d : %s against %s" % (u, b, wallets[u])

print "All tests passed."