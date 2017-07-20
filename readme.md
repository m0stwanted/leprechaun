# Leprechaun

## About

<p align="center">
    <img src="https://www.iconfinder.com/icons/1953329/download/png/128" alt="Leprechaun logo"/>
</p>

This project is money transfer application among internal users written as a test assignment.

## Installation and Launching

This application is a rather simple application of [Ratpack](https://ratpack.io/) Java Framework which produces
just one fat-jar. To install it from sources you just have to clone the stable version from the
repository and build it with `build.sh` script (Maven should be installed).

Maven build script will generate fat jar file named `leprechaun-server.jar` which is almost all you need to start an application:

    java -jar target/leprechaun-server.jar
	
The default port is `5060`. You may also customize ports, host, cache size and API prefix by editing
`app.properties` before compilation. File based configuration isn't supported at the time.

## Usage

### Accounts

#### Create account

<table>
    <tr>
        <td>HTTP method</td>
        <td>POST</td>
    </tr>
    <tr>
        <td>URI</td>
        <td>/account</td>
    </tr>
    <tr>
        <td>Request type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Request Body</td>
        <td>
            <p>JSON object with the following parameters:</p>
            <p>- name (String, mandatory. Represents account name.)</p>
            <p>- balance (Double, mandatory. Represents start balance for account.)</p>
            <p>{ "name": "Anakin Skywalker", "balance": "12000.50" }</p>
        </td>
    </tr>
    <tr>
        <td>Response Type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Response</td>
        <td>Created account in case of successful creation. Error description otherwise.
            <ul>
                <li>success: true | false</li>
                <li>message: description of status, for example: account doesn't exists.</li>
                <li>account: returns only if the account was successfully created.</li>
            </ul>
        </td>
    </tr>
</table>

#### Request account

<table>
    <tr>
        <td>HTTP method</td>
        <td>GET</td>
    </tr>
    <tr>
        <td>URI</td>
        <td>/account/:id</td>
    </tr>
    <tr>
        <td>Response Type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Response</td>
        <td>Account model if account exists or error description otherwise.
            <ul>
                <li>success: true | false</li>
                <li>message: description of status, for example: account doesn't exists.</li>
                <li>account: returns only if the account was founded.</li>
            </ul>
        </td>
    </tr>
</table>

#### Get account transactions

<table>
    <tr>
        <td>HTTP method</td>
        <td>GET</td>
    </tr>
    <tr>
        <td>URI</td>
        <td>/account/:id/transactions</td>
    </tr>
    <tr>
        <td>Response Type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Response</td>
        <td>Transactions list (income and outcome) for selected account.
            <ul>
                <li>success: true | false</li>
                <li>message: description of status, for example: account doesn't exists.</li>
                <li>transactions: returns only if the account was founded. Contains all related to account transactions.</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Limitations</td>
        <td>
            For the sake of simplicity, offset and limit parameters for pagination are not supported, so response contains
            all transactions. 
        </td>
    </tr>
</table>

### Transactions

#### Transfer money

<table>
    <tr>
        <td>HTTP method</td>
        <td>POST</td>
    </tr>
    <tr>
        <td>URI</td>
        <td>/transaction</td>
    </tr>
    <tr>
        <td>Request type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Request Body</td>
        <td>
            <p>JSON object with the following parameters:</p>
            <p>- from (Long, mandatory. Sender account id.)</p>
            <p>- to (Long, mandatory. Receiver account id.)</p>
            <p>- amount (Double, mandatory. Amount of money to send <from> account to <to> account.)</p>
            <p>{ "from": "1", "to": "3", "amount": "15.50" }</p>
        </td>
    </tr>
    <tr>
        <td>Response Type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Response</td>
        <td>Created transaction in case of successful creation. Error description otherwise.
            <ul>
                <li>success: true | false</li>
                <li>message: description of status, for example: not enough money.</li>
                <li>transaction: returns only if the transaction was successfully created.</li>
            </ul>
        </td>
    </tr>
</table>

#### Get transaction

<table>
    <tr>
        <td>HTTP method</td>
        <td>GET</td>
    </tr>
    <tr>
        <td>URI</td>
        <td>/transaction/:id</td>
    </tr>
    <tr>
        <td>Response Type</td>
        <td>application/json</td>
    </tr>
    <tr>
        <td>Response</td>
        <td>Transaction model if transaction exists or error description otherwise.
            <ul>
                <li>success: true | false</li>
                <li>message: description of status, for example: account doesn't exists.</li>
                <li>transaction: returns only if the transaction was founded.</li>
            </ul>
        </td>
    </tr>
</table>