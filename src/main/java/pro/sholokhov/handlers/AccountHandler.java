package pro.sholokhov.handlers;

import static ratpack.jackson.Jackson.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.models.Account;
import pro.sholokhov.services.AccountService;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

@Singleton
public class AccountHandler implements Handler {

  private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final static ObjectMapper mapper = new ObjectMapper();

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
        context.getRequest().getBody()
          .flatMap(raw -> {
            ObjectReader parser = mapper.readerFor(Map.class);
            Map<String, String> data = parser.readValue(raw.getText());
            String name = data.get("name");
            Double startBalance = Double.valueOf(data.get("startBalance"));

            // todo: validate params
            return accountService.create(name, startBalance);
          })
          .then(acc -> context.render(json(new AccountResponse(acc, true, "Account created"))));
      })
      .get(() -> {
        String userId = context.getPathTokens().get("id");
        if (userId == null) {
          // todo: send error response
          throw new IllegalArgumentException("You should specify <user id> parameter.");
        }

        Optional<Account> acc = accountService.findById(Long.valueOf(userId));
        AccountResponse response = acc.map(account -> new AccountResponse(account, true, "ok"))
          .orElseGet(() -> new AccountResponse(true, "Account doesn't exists"));

        context.render(json(response));
      })
    );
  }
}
