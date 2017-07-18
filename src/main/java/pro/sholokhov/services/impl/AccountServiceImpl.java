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

  private final AtomicLong nextId = new AtomicLong(0L);
  private final KVStorage<Long, Account> storage = new InMemoryStorage<>();

  @Override
  public Promise<Account> create(String name, Double startBalance) throws IllegalArgumentException {
    return Blocking.get(() -> {
      long id = nextId.incrementAndGet();
      Account acc = new Account(id, name, startBalance);
      storage.put(id, acc);
      return acc;
    });
  }

  @Override
  public Optional<Account> findById(Long accountId) {
    return storage.get(accountId);
  }

  @Override
  public Optional<Account> remove(Long uid) {
    return storage.remove(uid);
  }
}
