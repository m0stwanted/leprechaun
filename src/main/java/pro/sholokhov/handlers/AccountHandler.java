package pro.sholokhov.handlers;

import static ratpack.jackson.Jackson.json;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.services.AccountService;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.lang.invoke.MethodHandles;

@Singleton
public class AccountHandler implements Handler {

  private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private AccountService accountService;

  @Inject
  public AccountHandler(AccountService accountService) {
    this.accountService = accountService;
  }

  //

  @Override
  public void handle(Context context) throws Exception {
    context.byMethod(r -> r
      .post(() -> {
        // todo: new account
      })
      .get(() -> {
        String userId = context.getPathTokens().get("id");
        if (userId == null) {
          throw new IllegalArgumentException("You should specify <user id> parameter.");
        }
        context.render(json(accountService.findById(Long.valueOf(userId)).get()));
      })
    );
  }
}
