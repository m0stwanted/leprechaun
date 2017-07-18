package pro.sholokhov.handlers;

import static ratpack.jackson.Jackson.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.models.Account;
import pro.sholokhov.services.AccountService;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Singleton
public class AccountHandler implements Handler {

  private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final static ObjectMapper mapper = new ObjectMapper();
  private final static ObjectReader parser = mapper.readerFor(Map.class);

  private AccountService accountService;

  @Inject
  public AccountHandler(AccountService accountService) {
    this.accountService = accountService;
  }

  //

  @JsonInclude(JsonInclude.Include.NON_NULL)
  static class AccountResponse {
    private Account account;
    private Boolean success;
    private String message;

    AccountResponse(Account account, Boolean success, String message) {
      this.account = account;
      this.success = success;
      this.message = message;
    }

    AccountResponse(Boolean success, String message) {
      this.success = success;
      this.message = message;
    }

    public Account getAccount() {
      return account;
    }

    public Boolean getSuccess() {
      return success;
    }

    public String getMessage() {
      return message;
    }
  }

  //

  @Override
  public void handle(Context context) throws Exception {
    context.byMethod(r -> r
      .post(() -> {
        withNameAndBalance(context, (name, balance) -> accountService.create(name, balance))
          .then(a -> context.render(json(new AccountResponse(a, true, "Account created"))));
      })
      .get(() -> {
        withAccountId(context, (accId) -> {
          Optional<Account> acc = accountService.findById(accId);
          AccountResponse response = acc.map(account -> new AccountResponse(account, true, "ok"))
              .orElseGet(() -> new AccountResponse(false, "Account doesn't exists"));
          context.render(json(response));
        });
      })
      .delete(() -> {
        withAccountId(context, (accId) -> {
          Optional<Account> removed = accountService.remove(accId);
          AccountResponse response = removed.map(a -> new AccountResponse(a, true, "removed"))
              .orElseGet(() -> new AccountResponse(false, "Account doesn't exists"));
          context.render(json(response));
        });
      })
    );
  }

  // small helpers - it could be extended to be generics with variable arg list
  // and custom validation... but let it be simple for now
  //
  private Promise<Account> withNameAndBalance(Context ctx, BiFunction<String, Double, Promise<Account>> onArgValid) {
    return ctx.getRequest().getBody().flatMap(raw -> {
      Map<String, String> data = parser.readValue(raw.getText());

      String name = data.get("name");
      String balance = data.get("balance");

      if (StringUtils.isNotEmpty(name) && NumberUtils.isCreatable(balance)) {
        return onArgValid.apply(name, Double.valueOf(balance));
      } else {
        Exception e = new IllegalArgumentException("Invalid name or balance!");
        ctx.render(json(new AccountResponse(false, e.getMessage())));
        return Promise.error(e);
      }
    });
  }

  private void withAccountId(Context ctx, Consumer<Long> onArgValid) {
    String accountId = ctx.getPathTokens().get("id");
    if (NumberUtils.isCreatable(accountId)) {
      onArgValid.accept(Long.valueOf(accountId));
    } else {
      ctx.render(json(
        new AccountResponse(false, "Invalid accountId: " + accountId)
      ));
    }
  }

}
