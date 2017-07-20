package pro.sholokhov.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import pro.sholokhov.models.domain.Transaction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse extends AbstractResponse {

  private Transaction transaction;

  public TransactionResponse(Transaction transaction, Boolean success, String message) {
    super(success, message);
    this.transaction = transaction;
  }

  public TransactionResponse(Boolean success, String message) {
    super(success, message);
  }

  public Transaction getTransaction() {
    return transaction;
  }

}
