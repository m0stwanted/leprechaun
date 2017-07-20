package pro.sholokhov.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class Configuration {

  private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String PROPERTIES_FILE = "app.properties";
  private static Properties defaults = new Properties();

  static {
    defaults.setProperty(Names.PORT.val(), "5070");
    defaults.setProperty(Names.ADDRESS.val(), "localhost");
    defaults.setProperty(Names.API_PREFIX.val(), "api/v1");
  }

  public enum Names {
    PORT("server.port"),
    ADDRESS("server.address"),
    API_PREFIX("api.prefix");

    private String value;

    Names(String value) {
      this.value = value;
    }

    public String val() {
      return value;
    }

  }

  public static Properties loadConfig() {
    return loadConfig(PROPERTIES_FILE);
  }

  public static Properties loadConfig(String file) {
    Properties properties = new Properties(defaults);
    try(InputStream stream = Configuration.class.getClassLoader().getResourceAsStream(file)) {
      if (stream != null) {
        properties.load(stream);
      } else {
        log.warn("Properties file {} was not loaded. Defaults were applied.", file);
      }
    } catch (IOException e) {
      log.warn("Could not load properties from file {}. Defaults were applied.", file, e);
    }
    return properties;
  }

}
