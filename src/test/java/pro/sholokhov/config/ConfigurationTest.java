package pro.sholokhov.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pro.sholokhov.config.Configuration.Names.ADDRESS;
import static pro.sholokhov.config.Configuration.Names.API_PREFIX;
import static pro.sholokhov.config.Configuration.Names.PORT;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.util.Properties;

public class ConfigurationTest {

  @Test
  public void loadDefaultConfig() throws Exception {
    Properties config = Configuration.loadConfig();
    String port = config.getProperty(PORT.val());

    assertTrue("Port should contains only digits", NumberUtils.isDigits(port));
    assertEquals("5060", port);
    assertEquals("localhost", config.getProperty(ADDRESS.val()));
    assertEquals("api/v1", config.getProperty(API_PREFIX.val()));
  }

}