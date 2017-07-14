package pro.sholokhov;

import pro.sholokhov.config.Configuration;
import ratpack.server.RatpackServer;

public class TransactionServer {

  private Configuration config;

  public TransactionServer(Configuration config) {
    this.config = config;
  }

  public void run() throws Exception {
    RatpackServer.start(server -> server
        .handlers(chain -> chain
            .get(ctx -> ctx.render("Hello World!"))
            .get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
        )
    );
  }

}
