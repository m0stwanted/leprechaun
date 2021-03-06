package pro.sholokhov.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.models.domain.Transaction;
import pro.sholokhov.models.exceptions.InactiveAccountException;
import pro.sholokhov.models.exceptions.NotEnoughMoneyException;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.TransactionService;
import pro.sholokhov.storage.KVStorage;
import pro.sholokhov.storage.impl.InMemoryStorage;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class TransactionServiceImpl implements TransactionService {

  private final AtomicLong nextId = new AtomicLong(0L);
  private final KVStorage<Long, Transaction> storage = new InMemoryStorage<>();

  private final Exception inconsistency = new IllegalStateException("Inconsistent storage state!");

  private AccountService accountService;

  @Inject
  public TransactionServiceImpl(AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public Promise<Transaction> create(Long from, Long to, Double amount) throws IllegalStateException {
    return Blocking.get(() -> {
      Account fromAcc = accountService.findById(from).orElseThrow(() -> inconsistency);
      if (!fromAcc.isActive()) {
        throw new InactiveAccountException("Sender account is not active!");
      }

      Account toAcc = accountService.findById(to).orElseThrow(() -> inconsistency);
      if (!toAcc.isActive()) {
        throw new InactiveAccountException("Receiver account is not active!");
      }

      return doTransfer(fromAcc, toAcc, amount);
    });
  }

  @Override
  public Optional<Transaction> findById(Long transactionId) {
    return storage.get(transactionId);
  }

  //

  private Transaction doTransfer(Account from, Account to, Double amount) throws IllegalStateException  {
    BigDecimal charge = new BigDecimal(amount);
    if (from.getBalance().subtract(charge).compareTo(BigDecimal.ZERO) >= 0) {
      synchronized (from.getMutex()) {
        if (from.getBalance().subtract(charge).compareTo(BigDecimal.ZERO) >= 0) {
          long tid = nextId.incrementAndGet();
          from.updateBalance(charge.negate());
          to.updateBalance(charge);
          Transaction t = new Transaction(tid, from.getId(), to.getId(), amount);
          from.addTransaction(t);
          to.addTransaction(t);
          storage.put(tid, t);
          return t;
        } else {
          throw new NotEnoughMoneyException("Transfer failed: not enough money.");
        }
      }
    } else {
      throw new NotEnoughMoneyException("Transfer failed: not enough money.");
    }
  }

}
