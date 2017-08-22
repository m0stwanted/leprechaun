package pro.sholokhov.services.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.models.domain.Transaction;
import pro.sholokhov.models.exceptions.InactiveAccountException;
import pro.sholokhov.models.exceptions.NotEnoughMoneyException;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.TransactionService;
import ratpack.test.exec.ExecHarness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

/**
 * @author Artem Sholokhov
 */
public class TransactionServiceImplTest {

  private static final AccountService accountService = new AccountServiceImpl();
  private static final TransactionService transactionService = new TransactionServiceImpl(accountService);
  private static final List<Long> ids = new CopyOnWriteArrayList<>();

  private static final double richUserOneStartBalance = 5000.0;
  private static final double richUserTwoStartBalance = 15000.0;

  @BeforeClass
  public static void setUp() throws Exception {
    Random r = new Random();
    int accounts = 10;

    ExecHarness.harness(1).run(e -> {
      IntStream.range(0, accounts).forEach(idx ->
          accountService.create(
              "Test User " + idx,
              // poor user with index 0
              idx == 0 ? 0 : (r.nextDouble() * 100 + 50)
          )
          .then(a -> ids.add(a.getId()))
      );
      accountService.create("Rich User 1", richUserOneStartBalance).then(a -> ids.add(a.getId()));
      accountService.create("Rich User 2", richUserTwoStartBalance).then(a -> ids.add(a.getId()));
    });
  }

  @Test
  public void shouldTransferMoneySuccessfully() throws Exception {
    Long sender = ids.get(ids.size() - 1);
    Long receiver = ids.get(ids.size() - 2);
    double amount = 2500.0;
    BigDecimal bAmount = BigDecimal.valueOf(amount);

    Transaction t =
        ExecHarness.yieldSingle(e -> transactionService.create(sender, receiver, amount))
            .getValue();

    assertTrue("Invalid sender id", sender.equals(t.getFrom()));
    assertTrue("Invalid receiver id", receiver.equals(t.getTo()));
    assertTrue("Invalid amount", t.getAmount().compareTo(bAmount) == 0);

    Account snd = accountService.findById(sender).orElseThrow(() -> new IllegalStateException("Account not found!"));
    Account rcv = accountService.findById(receiver).orElseThrow(() -> new IllegalStateException("Account not found!"));

    assertEquals("Invalid balance for sender", snd.getBalance(), BigDecimal.valueOf(richUserTwoStartBalance).subtract(bAmount));
    assertEquals("Invalid balance for receiver", rcv.getBalance(), BigDecimal.valueOf(richUserOneStartBalance).add(bAmount));
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void shouldFailWithNotEnoughMoney() throws Throwable {
    Long poorAccId = ids.get(0);
    throw ExecHarness.yieldSingle(e ->
      transactionService.create(poorAccId, 5L, 200.0)).getThrowable();
  }

  @Test(expected = InactiveAccountException.class)
  public void shouldFailWithInactivateAccountExSender() throws Throwable {
    Long dId = ids.get(1);
    accountService.remove(dId);
    throw ExecHarness.yieldSingle(e ->
      transactionService.create(dId, 5L, 200.0)).getThrowable();
  }

  @Test(expected = InactiveAccountException.class)
  public void shouldFailWithInactivateAccountExReceiver() throws Throwable {
    Long dId = ids.get(1);
    accountService.remove(dId);
    throw ExecHarness.yieldSingle(e ->
        transactionService.create(5L, dId, 200.0)).getThrowable();
  }

  @Test
  public void findById() throws Exception {
    Long sender = ids.get(1);
    Long receiver = ids.get(2);
    double amount = 10.0;
    BigDecimal bAmount = BigDecimal.valueOf(amount);

    Transaction t =
        ExecHarness.yieldSingle(e -> transactionService.create(sender, receiver, amount))
            .getValue();

    Optional<Transaction> byId = transactionService.findById(t.getId());
    Transaction foundedTr =
        byId.orElseThrow(() -> new IllegalStateException("Transaction not found!"));

    assertTrue("Invalid sender id", sender.equals(foundedTr.getFrom()));
    assertTrue("Invalid receiver id", receiver.equals(foundedTr.getTo()));
    assertTrue("Invalid amount", foundedTr.getAmount().compareTo(bAmount) == 0);
  }

}