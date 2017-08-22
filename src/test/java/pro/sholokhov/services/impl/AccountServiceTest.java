package pro.sholokhov.services.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.services.AccountService;
import ratpack.exec.Promise;
import ratpack.test.exec.ExecHarness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * @author Artem Sholokhov
 */
public class AccountServiceTest {

  private AccountService accountService = new AccountServiceImpl();

  @Test
  public void shouldCreateNewAccount() throws Exception {
    BigDecimal startBalance = new BigDecimal(400.5);
    String name = "John Snow";

    Account account = ExecHarness
      .yieldSingle(e -> accountService.create(name, startBalance.doubleValue()))
      .getValue();

    assertTrue("Invalid name", account.getName().equals(name));
    assertTrue("Invalid balance", account.getBalance().equals(startBalance));
  }

  @Test
  public void shouldFindAccountById() throws Exception {
    BigDecimal startBalance = new BigDecimal(100.0);
    String name = "Luke Skywalker";

    Long id = ExecHarness
      .yieldSingle(e -> accountService.create(name, startBalance.doubleValue()))
      .getValue()
      .getId();

    Optional<Account> opt = accountService.findById(id);
    Account account = opt.orElseThrow(() -> new IllegalStateException("Account not found!"));

    assertTrue("Invalid name", account.getName().equals(name));
    assertTrue("Invalid balance", account.getBalance().compareTo(startBalance) == 0);
  }

  @Test
  public void shouldDeactivateAccount() throws Exception {
    int accCount = 5;
    Random r = new Random();
    List<Long> ids = new CopyOnWriteArrayList<>();
    IllegalStateException accountNotFoundEx = new IllegalStateException("Account not found!");

    ExecHarness.harness(2)
      .run(e -> IntStream.range(0, accCount).forEach(idx ->
        accountService
          .create("Test Account " + idx, r.nextDouble())
          .then(a -> ids.add(a.getId())))
      );

    assertTrue("Invalid accounts number", ids.size() == accCount);

    boolean allActive = ids.stream()
        .map(accountService::findById)
        .map(Optional::get)
        .allMatch(Account::isActive);

    assertTrue("All accounts should be active", allActive);
    Long randomId = ids.get(r.nextInt(ids.size()));

    Optional<Account> removed = accountService.remove(randomId);
    Account rmAcc = removed.orElseThrow(() -> accountNotFoundEx);
    assertTrue("Deleted account still activated", !rmAcc.isActive());
  }

  @Test
  public void shouldCheckIsAccountActive() throws Exception {
    Account test = ExecHarness.yieldSingle(e -> accountService.create("Test", 120.0)).getValue();
    assertTrue("Account should be active", accountService.isAccountActive(test.getId()));

    accountService.remove(test.getId());
    assertFalse("Account should be deactivated", accountService.isAccountActive(test.getId()));
  }

}