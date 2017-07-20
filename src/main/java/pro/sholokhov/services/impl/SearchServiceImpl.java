package pro.sholokhov.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pro.sholokhov.models.search.Filter;
import pro.sholokhov.models.search.SearchResult;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.SearchService;
import pro.sholokhov.services.TransactionService;

import java.util.Collections;
import java.util.List;

@Singleton
public class SearchServiceImpl implements SearchService {

    private AccountService accountService;
    private TransactionService transactionService;

    @Inject
    public SearchServiceImpl(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    //

    @Override
    public List<SearchResult> search(Filter filter) {
        return Collections.emptyList();
    }

}
