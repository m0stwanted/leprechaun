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
  public Promise<Account> create(String name, Double startBalance) {
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
  public Optional<Account> remove(Long accountId) {
    Optional<Account> opt = storage.get(accountId);
    opt.ifPresent(Account::deactivate);
    return opt;
  }

  @Override
  public Boolean isAccountActive(Long accountId) {
    Optional<Account> account = storage.get(accountId);
    if (account.isPresent()) {
      Account acc = account.get();
      return acc.isActive();
    }
    return false;
  }
}
