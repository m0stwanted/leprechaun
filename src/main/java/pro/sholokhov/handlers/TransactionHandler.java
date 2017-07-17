package pro.sholokhov.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.services.TransactionService;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.lang.invoke.MethodHandles;

@Singleton
public class TransactionHandler implements Handler {

  private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private TransactionService transactionService;

  @Inject
  public TransactionHandler(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @Override
  public void handle(Context context) throws Exception {
    // todo: implementation
  }
}
