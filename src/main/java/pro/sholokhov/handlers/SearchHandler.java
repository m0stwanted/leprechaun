package pro.sholokhov.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.TransactionService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.service.Service;

@Singleton
public class SearchHandler implements Handler, Service {

  private AccountService accountService;
  private TransactionService transactionService;

  @Inject
  public SearchHandler(AccountService accountService, TransactionService transactionService) {
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  @Override
  public void handle(Context ctx) throws Exception {
    // todo: search transactions
  }

}
