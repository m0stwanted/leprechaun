package pro.sholokhov.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import pro.sholokhov.models.response.AccountResponse;
import pro.sholokhov.models.response.TransactionResponse;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.http.TestHttpClient;

import java.io.IOException;
import java.util.Random;

/**
 * @author Artem Sholokhov
 */
public class TestHelpers {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final Random r = new Random();

  public static TransactionResponse createTransaction(
      TestHttpClient httpClient,
      String apiPrefix,
      String from,
      String to,
      String amount
  ) throws IOException
  {
    String body =
        String.format("{ \"from\": \"%s\", \"to\": \"%s\", \"amount\": \"%s\" }", from, to, amount);
    ReceivedResponse resp = httpClient
        .requestSpec(s -> s.body(b -> b.text(body)))
        .post(apiPrefix + "/transaction");
    return mapper.readValue(resp.getBody().getText(), TransactionResponse.class);
  }

  public static AccountResponse createAccount(
      TestHttpClient httpClient,
      String apiPrefix,
      boolean valid
  ) throws Exception
  {
    String name = "Test Account " + r.nextInt(100);
    String balance = valid ? String.valueOf(r.nextDouble() * 100 + 50) : "123.2aba";
    String body = String.format("{ \"name\": \"%s\", \"balance\": \"%s\" }", name, balance);

    ReceivedResponse postRaw = httpClient
        .requestSpec(s -> s.body(b -> b.text(body)))
        .post(apiPrefix + "/account");

    return mapper.readValue(postRaw.getBody().getText(), AccountResponse.class);
  }

}
