package pro.sholokhov.services.impl;

import com.google.inject.Singleton;
import pro.sholokhov.models.Account;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.storage.KVStorage;
import pro.sholokhov.storage.impl.InMemoryStorage;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class AccountServiceImpl implements AccountService {

  private final AtomicLong nextId = new AtomicLong(1L);
  private final KVStorage<Long, Account> storage = new InMemoryStorage<>();

  @Override
  public Promise<Account> create(String name, Double startBalance) throws IllegalArgumentException {
    return Blocking.get(() -> new Account(nextId.incrementAndGet(), name, startBalance));
  }

  @Override
  public Optional<Account> findById(Long accountId) {
    // todo: accounts storage / cache
    return Optional.of(new Account(accountId, "test", 100500.0));
  }

}
