package pro.sholokhov.services;

import com.google.inject.ImplementedBy;
import pro.sholokhov.models.Account;
import pro.sholokhov.services.impl.AccountServiceImpl;
import ratpack.exec.Promise;

import java.util.Optional;

@ImplementedBy(AccountServiceImpl.class)
public interface AccountService {

  Promise<Account> create(String name, Double startBalance) throws IllegalArgumentException;

  Optional<Account> findById(Long accountId);

  Optional<Account> remove(Long uid);

}
