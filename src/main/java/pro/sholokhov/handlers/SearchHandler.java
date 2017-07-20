package pro.sholokhov.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pro.sholokhov.models.search.Filter;
import pro.sholokhov.services.AccountService;
import pro.sholokhov.services.SearchService;
import pro.sholokhov.services.TransactionService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.service.Service;
import ratpack.util.MultiValueMap;

@Singleton
public class SearchHandler implements Handler, Service {

  private SearchService searchService;

  @Inject
  public SearchHandler(SearchService searchService) {
    this.searchService = searchService;
  }

  @Override
  public void handle(Context ctx) throws Exception {
    MultiValueMap<String, String> params = ctx.getRequest().getQueryParams();
    // ...
  }

}
