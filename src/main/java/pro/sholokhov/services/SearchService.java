package pro.sholokhov.services;

import com.google.inject.ImplementedBy;
import pro.sholokhov.models.search.Filter;
import pro.sholokhov.models.search.SearchResult;
import pro.sholokhov.services.impl.SearchServiceImpl;

import java.util.List;

@ImplementedBy(SearchServiceImpl.class)
public interface SearchService {

  List<SearchResult> search(Filter filter);

}
