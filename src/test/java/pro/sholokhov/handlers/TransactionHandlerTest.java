package pro.sholokhov.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pro.sholokhov.config.Configuration.Names.API_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import pro.sholokhov.config.Configuration;
import pro.sholokhov.helpers.TestHelpers;
import pro.sholokhov.models.domain.Transaction;
import pro.sholokhov.models.response.TransactionResponse;
import pro.sholokhov.server.RevolutServer;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.http.TestHttpClient;

import java.util.Properties;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Artem Sholokhov
 */
public class TransactionHandlerTest {

  private static EmbeddedApp app;
  private static String apiPrefix;

  private static final Random r = new Random();
  private static final ObjectMapper mapper = new ObjectMapper();

  @BeforeClass
  public static void setUp() throws Exception {
    Properties config = Configuration.loadConfig();
    apiPrefix = config.getProperty(API_PREFIX.val());
    app = EmbeddedApp.fromServer(new RevolutServer().start("7072"));

    TestHttpClient httpClient = app.getHttpClient();
    IntStream.range(0, 10).forEach(idx -> {
      String name = "Test Account " + idx;
      String balance = String.valueOf(r.nextDouble() * 100 + 50);
      String body = String.format("{ \"name\": \"%s\", \"balance\": \"%s\" }", name, balance);

      httpClient
        .requestSpec(s -> s.body(b -> b.text(body)))
        .post(apiPrefix + "/account");
    });

  }

  @Test
  public void shouldSuccessfullyCreateTransaction() throws Exception {
    TransactionResponse response = TestHelpers
        .createTransaction(app.getHttpClient(), apiPrefix, "1", "5", "17.48");
    assertTrue("Invalid status", response.getSuccess());
  }

  @Test
  public void shouldFailedWithInvalidAccountId() throws Exception {
    TransactionResponse response = TestHelpers
        .createTransaction(app.getHttpClient(), apiPrefix, "aba", "2", "17.48");
    assertFalse("Invalid status", response.getSuccess());
  }

  @Test
  public void shouldFailedWithInvalidAmount() throws Exception {
    TransactionResponse response = TestHelpers
        .createTransaction(app.getHttpClient(), apiPrefix, "1", "7", "17av1a.48");
    assertFalse("Invalid status", response.getSuccess());
  }

  @Test
  public void shouldGetTransactionById() throws Exception {
    TestHttpClient httpClient = app.getHttpClient();

    String from = "5";
    String to = "1";
    String amount = "17.48";

    TransactionResponse response = TestHelpers
        .createTransaction(app.getHttpClient(), apiPrefix, from, to, amount);
    Long tid = response.getTransaction().getId();
    ReceivedResponse getRaw = httpClient.get(apiPrefix + "/transaction/" + tid);
    TransactionResponse resp =
        mapper.readValue(getRaw.getBody().getText(), TransactionResponse.class);

    Transaction transaction = resp.getTransaction();

    assertEquals("Invalid transaction id", tid, transaction.getId());
    assertEquals("Invalid sender id", from, transaction.getFrom().toString());
    assertEquals("Invalid receiver id", to, transaction.getTo().toString());
    assertEquals("Invalid transaction amount", amount, transaction.getAmount().toString());
  }

}