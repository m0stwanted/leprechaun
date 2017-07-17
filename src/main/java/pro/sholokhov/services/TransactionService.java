package pro.sholokhov.services;

import com.google.inject.ImplementedBy;
import pro.sholokhov.models.Transaction;
import pro.sholokhov.services.impl.TransactionServiceImpl;
import ratpack.exec.Promise;

import java.util.Optional;

@ImplementedBy(TransactionServiceImpl.class)
public interface TransactionService {

  Promise<Transaction> create() throws IllegalArgumentException;

  Optional<Transaction> findById(String transactionId);

}
