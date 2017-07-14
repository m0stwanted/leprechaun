package pro.sholokhov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.sholokhov.config.Configuration;
import pro.sholokhov.config.impl.DefaultConfiguration;

public class RevolutApp {

  private static final Logger logger = LoggerFactory.getLogger(RevolutApp.class);

  public static void main(String[] args) throws Exception {
    try {
      logger.info("*** Initializing ***");
      Configuration conf = new DefaultConfiguration();
      TransactionServer server = new TransactionServer(conf);
      server.run();
    } finally {
      logger.info("*** Application terminated ***");
    }
  }

}
