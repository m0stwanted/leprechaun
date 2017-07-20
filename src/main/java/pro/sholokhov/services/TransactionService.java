package pro.sholokhov.services;

import com.google.inject.ImplementedBy;
import pro.sholokhov.models.domain.Transaction;
import pro.sholokhov.services.impl.TransactionServiceImpl;
import ratpack.exec.Promise;

import java.util.List;
import java.util.Optional;

@ImplementedBy(TransactionServiceImpl.class)
public interface TransactionService {

  Promise<Transaction> create(Long from, Long to, Double amount) throws IllegalArgumentException;

  Optional<Transaction> findById(Long transactionId);

  Optional<List<Transaction>> filter();

}
