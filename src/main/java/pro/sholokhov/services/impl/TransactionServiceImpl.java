package pro.sholokhov.services.impl;

import com.google.inject.Singleton;
import org.joda.time.DateTime;
import pro.sholokhov.models.Transaction;
import pro.sholokhov.services.TransactionService;
import ratpack.exec.Promise;

import java.util.Optional;

@Singleton
public class TransactionServiceImpl implements TransactionService {

  @Override
  public Promise<Transaction> create() throws IllegalArgumentException {
    return null;
  }

  @Override
  public Optional<Transaction> findById(String transactionId) {
    return Optional.of(new Transaction(1L, new DateTime(), 1L, 2L, 50.0));
  }

}
