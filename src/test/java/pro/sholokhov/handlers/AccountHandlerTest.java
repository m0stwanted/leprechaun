package pro.sholokhov.handlers;

import static org.junit.Assert.*;
import static pro.sholokhov.config.Configuration.Names.API_PREFIX;
import static pro.sholokhov.config.Configuration.Names.PORT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pro.sholokhov.config.Configuration;
import pro.sholokhov.helpers.TestHelpers;
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.models.response.AccountResponse;
import pro.sholokhov.models.response.TransactionListResponse;
import pro.sholokhov.server.RevolutServer;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.impl.AccountServiceImpl;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.http.TestHttpClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Artem Sholokhov
 */
public class AccountHandlerTest {

  private static EmbeddedApp app;
  private static String apiPrefix;
  private static final ObjectMapper mapper = new ObjectMapper();

  @BeforeClass
  public static void setUp() throws Exception {
    Properties config = Configuration.loadConfig();
    apiPrefix = config.getProperty(API_PREFIX.val());
    app = EmbeddedApp.fromServer(new RevolutServer().start("7071"));
  }

  @Test
  public void shouldCreateAccountViaPostRequest() throws Exception {
    AccountResponse response = TestHelpers
        .createAccount(app.getHttpClient(), apiPrefix, true);
    assertTrue("Invalid response status", response.getSuccess());
  }

  @Test
  public void shouldFailAccountCreationWithInvalidArguments() throws Exception {
    AccountResponse response = TestHelpers
        .createAccount(app.getHttpClient(), apiPrefix, false);
    assertFalse("Invalid response status", response.getSuccess());
  }

  @Test
  public void shouldGetAccountById() throws Exception {
    TestHttpClient httpClient = app.getHttpClient();
    AccountResponse response = TestHelpers
        .createAccount(app.getHttpClient(), apiPrefix, true);

    Long id = response.getAccount().getId();
    ReceivedResponse getRaw = httpClient.get(apiPrefix + "/account/" + id);
    AccountResponse getResponse =
        mapper.readValue(getRaw.getBody().getText(), AccountResponse.class);

    assertTrue("Invalid response status", getResponse.getSuccess());
    assertEquals("Invalid account id", id, getResponse.getAccount().getId());
  }

  @Test
  public void shouldGetRelatedTransactionsForAccount() throws Exception {
    TestHttpClient httpClient = app.getHttpClient();
    Account accOne = TestHelpers
        .createAccount(httpClient, apiPrefix, true).getAccount();
    Account accTwo = TestHelpers
        .createAccount(httpClient, apiPrefix, true).getAccount();

    String amount = "5.5";
    IntStream.range(0, 3).forEach(idx -> {
      try {
        TestHelpers
            .createTransaction(
                httpClient,
                apiPrefix,
                String.valueOf(accOne.getId()),
                String.valueOf(accTwo.getId()),
                amount
            );
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    ReceivedResponse rawResponse =
        httpClient.get(apiPrefix + "/account/" + accOne.getId() + "/transactions");
    TransactionListResponse listResponse =
        mapper.readValue(rawResponse.getBody().getText(), TransactionListResponse.class);

    assertTrue("Invalid transactions count", listResponse.getTransactions().size() == 3);
    listResponse.getTransactions().forEach(t -> {
      assertEquals("Invalid sender id", accOne.getId().toString(), t.getFrom().toString());
      assertEquals("Invalid receiver id", accTwo.getId().toString(), t.getTo().toString());
      assertTrue("Invalid amount", new BigDecimal(amount).compareTo(t.getAmount()) == 0);
    });
  }

}