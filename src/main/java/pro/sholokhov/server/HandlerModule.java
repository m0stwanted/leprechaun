package pro.sholokhov.server;

import com.google.inject.AbstractModule;
import pro.sholokhov.handlers.AccountHandler;
import pro.sholokhov.handlers.ErrorHandler;
import pro.sholokhov.handlers.HelpHandler;
import pro.sholokhov.handlers.SearchHandler;
import pro.sholokhov.handlers.TransactionHandler;

public class HandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountHandler.class);
        bind(TransactionHandler.class);
        bind(SearchHandler.class);
        bind(ErrorHandler.class);
        bind(HelpHandler.class);
    }
}