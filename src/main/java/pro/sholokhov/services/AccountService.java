package pro.sholokhov.services;

import com.google.inject.ImplementedBy;
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.services.impl.AccountServiceImpl;
import ratpack.exec.Promise;

import java.util.Optional;

@ImplementedBy(AccountServiceImpl.class)
public interface AccountService {

  Promise<Account> create(String name, Double startBalance);

  Optional<Account> findById(Long accountId);

  Optional<Account> remove(Long accountId);

  Boolean isAccountActive(Long accountId);

}
