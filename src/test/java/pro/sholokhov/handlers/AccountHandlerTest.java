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
import pro.sholokhov.models.domain.Account;
import pro.sholokhov.models.response.AccountResponse;
import pro.sholokhov.server.RevolutServer;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.impl.AccountServiceImpl;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.http.TestHttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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
    app = EmbeddedApp.fromServer(new RevolutServer().start());
  }

  @Test
  public void shouldCreateAccountViaPostRequest() throws Exception {
    AccountResponse response = createAccount(true);
    assertTrue("Invalid response status", response.getSuccess());
  }

  @Test
  public void shouldFailAccountCreationWithInvalidArguments() throws Exception {
    AccountResponse response = createAccount(false);
    assertFalse("Invalid response status", response.getSuccess());
  }

  @Test
  public void shouldGetAccountById() throws Exception {
    TestHttpClient httpClient = app.getHttpClient();
    AccountResponse response = createAccount(true);
    Long id = response.getAccount().getId();

    ReceivedResponse getRaw = httpClient.get(apiPrefix + "/account/" + id);
    AccountResponse getResponse =
        mapper.readValue(getRaw.getBody().getText(), AccountResponse.class);

    assertTrue("Invalid response status", getResponse.getSuccess());
    assertEquals("Invalid account id", id, getResponse.getAccount().getId());
  }

  //

  private AccountResponse createAccount(boolean valid) throws Exception {
    Random r = new Random();
    String name = "Test Account " + r.nextInt(100);
    String balance = valid ? String.valueOf(r.nextDouble() * 100 + 50) : "123.2aba";
    TestHttpClient httpClient = app.getHttpClient();

    String body = String.format("{ \"name\": \"%s\", \"balance\": \"%s\" }", name, balance);
    ReceivedResponse postRaw = httpClient
      .requestSpec(s -> s.body(b -> b.text(body)))
      .post(apiPrefix + "/account");

    return mapper.readValue(postRaw.getBody().getText(), AccountResponse.class);
  }

}