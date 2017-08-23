package pro.sholokhov.server;

import static pro.sholokhov.config.Configuration.Names.ADDRESS;
import static pro.sholokhov.config.Configuration.Names.API_PREFIX;
import static pro.sholokhov.config.Configuration.Names.PORT;

import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Names;
import pro.sholokhov.config.Configuration;
import pro.sholokhov.handlers.AccountHandler;
import pro.sholokhov.handlers.ErrorHandler;
import pro.sholokhov.handlers.HelpHandler;
import pro.sholokhov.handlers.TransactionHandler;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.TransactionService;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;

import java.util.Properties;

/**
 * @author Artem Sholokhov
 */
public class RevolutServer {

  public RatpackServer start(String customPort) throws Exception {
    Properties config = Configuration.loadConfig();
    return RatpackServer.start(server -> server
        .serverConfig(serverConfigBuilder -> serverConfigBuilder
            .props(ImmutableMap.of(
                PORT.val(), customPort != null ? customPort : config.getProperty(PORT.val()),
                ADDRESS.val(), config.getProperty(ADDRESS.val())))
        )
        .registry(Guice.registry(bindings -> bindings
            .binder(binder -> Names.bindProperties(binder, config))
            .module(HandlerModule.class)
            .bind(AccountService.class)
            .bind(TransactionService.class))
        )
        .handlers(chain -> { chain
            .register(registry -> registry.add(ErrorHandler.class))
            .prefix(config.getProperty(API_PREFIX.val()), apiChain -> apiChain
                .get("help", HelpHandler.class)
                .prefix("account", a -> a
                    .post(AccountHandler.class)
                    .get("/:id", AccountHandler.class)
                    .path(":id/transactions", AccountHandler.class)
                )
                .prefix("transaction", a -> a
                    .post(TransactionHandler.class)
                    .path("/:id", TransactionHandler.class)
                )
            );
        })
    );
  }

}
