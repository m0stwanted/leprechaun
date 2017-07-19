package pro.sholokhov.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.time.DateTime;
import pro.sholokhov.models.Account;
import pro.sholokhov.models.Transaction;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.TransactionService;
import pro.sholokhov.storage.KVStorage;
import pro.sholokhov.storage.impl.InMemoryStorage;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

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
  public Promise<Transaction> create(Long from, Long to, Double amount) throws IllegalArgumentException {
    return Blocking.get(() -> {
      Account fromAcc = accountService.findById(from).orElseThrow(() -> inconsistency);
      Account toAcc = accountService.findById(to).orElseThrow(() -> inconsistency);
      return doTransfer(fromAcc, toAcc, amount).orElseThrow(() -> new IllegalStateException("Can't proceed transaction"));
    });
  }

  private Optional<Transaction> doTransfer(Account from, Account to, Double amount) {
    // todo: thread safe transfer among accounts
    return Optional.empty();
  }

  @Override
  public Optional<Transaction> findById(Long transactionId) {
    return Optional.empty();
  }

  @Override
  public Optional<List<Transaction>> filter() {
    return Optional.empty();
  }
}
