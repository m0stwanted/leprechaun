package pro.sholokhov.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.models.response.AccountResponse;
import pro.sholokhov.models.response.TransactionListResponse;
import pro.sholokhov.services.AccountService;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static ratpack.jackson.Jackson.json;

@Singleton
public class AccountHandler implements Handler {

  private final static ObjectMapper mapper = new ObjectMapper();
  private final static ObjectReader parser = mapper.readerFor(Map.class);

  // GC-optimized common responses
  private final static AccountResponse accountNotExists =
    new AccountResponse(false, "Account doesn't exists");

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
        withNameAndBalance(context, (name, balance) -> accountService.create(name, balance))
          .onError(e -> context.render(json(new AccountResponse(false, e.getMessage()))))
          .map(a -> new AccountResponse(a, true, "Account created"))
          .then(a -> context.render(json(a)));
      })
      .get(() -> {
        withAccountId(context, (accId) -> {
          Optional<Account> opt = accountService.findById(accId).filter(Account::isActive);
          opt.ifPresent(account -> {
            if (context.getPathBinding().getBoundTo().endsWith("transactions")) {
              context.render(json(new TransactionListResponse(account.getRelatedTransactions(), true, "Ok")));
            } else {
              context.render(json(new AccountResponse(account, true, "Ok")));
            }
          });

          if (!opt.isPresent()) {
            context.render(json(accountNotExists));
          }
        });
      })
      .delete(() -> {
        withAccountId(context, (accId) -> {
          AccountResponse response = accountService.remove(accId)
            .map(a -> new AccountResponse(a, true, "Account removed"))
            .orElse(accountNotExists);
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
        return Promise.error(new IllegalArgumentException("Invalid name or balance!"));
      }
    });
  }

  private void withAccountId(Context ctx, Consumer<Long> onArgValid) {
    String accountId = ctx.getPathTokens().get("id");
    if (NumberUtils.isCreatable(accountId)) {
      onArgValid.accept(Long.valueOf(accountId));
    } else {
      ctx.render(json(new AccountResponse(false, "Invalid accountId: " + accountId)));
    }
  }

}
