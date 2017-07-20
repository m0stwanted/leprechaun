package pro.sholokhov.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.models.domain.Transaction;
import pro.sholokhov.models.response.TransactionResponse;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.TransactionService;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static ratpack.jackson.Jackson.json;

@Singleton
public class TransactionHandler implements Handler {

  private final static ObjectMapper mapper = new ObjectMapper();
  private final static ObjectReader parser = mapper.readerFor(Map.class);

  // GC-optimized common responses
  private final static TransactionResponse transactionNotExists =
    new TransactionResponse(false, "Transaction doesn't exists");

  private AccountService accountService;
  private TransactionService transactionService;

  @Inject
  public TransactionHandler(AccountService accountService, TransactionService transactionService) {
    this.accountService = accountService;
    this.transactionService = transactionService;
  }

  @Override
  public void handle(Context context) throws Exception {
    context.byMethod(r -> r
      .post(() -> {
        parseRequest(context, (params) -> {
          Long from = Long.valueOf(params.get("from"));
          Long to = Long.valueOf(params.get("to"));
          Double amount = Double.valueOf(params.get("amount"));
          return transactionService.create(from, to, amount);
        })
        .onError(e -> context.render(json(new TransactionResponse(false, e.getMessage()))))
        .then(t -> context.render(json(new TransactionResponse(t, true, "Transaction created"))));
      })
      .get(() -> {
        withTransactionId(context, (trId) -> {
          TransactionResponse response = transactionService.findById(trId)
            .map(t -> new TransactionResponse(t, true, "Ok"))
            .orElse(transactionNotExists);
          context.render(json(response));
        });
      })
    );
  }

  private Promise<Transaction> parseRequest(Context ctx, Function<Map<String, String>, Promise<Transaction>> onArgValid) {
    return ctx.getRequest().getBody().flatMap(raw -> {
      Map<String, String> data = parser.readValue(raw.getText());

      String from = data.get("from");
      String to = data.get("to");
      String amount = data.get("amount");

      try {
        Map<String, String> params = parseParams(from, to, amount);
        return onArgValid.apply(params);
      } catch (Exception e) {
        ctx.render(json(new TransactionResponse(false, e.getMessage())));
        return Promise.error(e);
      }
    });
  }

  private void withTransactionId(Context ctx, Consumer<Long> onArgValid) {
    String transactionId = ctx.getPathTokens().get("id");
    if (NumberUtils.isCreatable(transactionId)) {
      onArgValid.accept(Long.valueOf(transactionId));
    } else {
      ctx.render(json(new TransactionResponse(false, "Invalid transactionId: " + transactionId)));
    }
  }

  // parse and validate parameters for transaction creation
  //
  private Map<String, String> parseParams(String from, String to, String amount) throws IllegalArgumentException {
    Map<String, String> params = new HashMap<>();

    // it could be extended to use custom or external validators with pre configured rules
    //
    if (NumberUtils.isCreatable(from) && accountService.isAccountActive(Long.valueOf(from))) {
      params.put("from", from);
    } else throw new IllegalArgumentException("Invalid <from> field or account doesn't exists.");

    if (NumberUtils.isCreatable(to) && accountService.isAccountActive(Long.valueOf(to))) {
      params.put("to", to);
    } else throw new IllegalArgumentException("Invalid <to> field or account doesn't exists.");

    if (from.equals(to)) {
      throw new IllegalArgumentException("Sender and receiver couldn't be the same.");
    }

    if (NumberUtils.isCreatable(amount) && Double.valueOf(amount) > 0) {
      params.put("amount", amount);
    } else throw new IllegalArgumentException("Invalid value of <amount> field.");

    return params;
  }

}
