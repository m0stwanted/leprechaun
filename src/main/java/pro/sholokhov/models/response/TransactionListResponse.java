package pro.sholokhov.models.response;

import pro.sholokhov.models.domain.Transaction;

import java.util.List;

public class TransactionListResponse extends AbstractResponse {

    private List<Transaction> transactions;

    public TransactionListResponse(List<Transaction> transactions, Boolean success, String message) {
        super(success, message);
        this.transactions = transactions;
    }

    public TransactionListResponse(Boolean success, String message) {
        super(success, message);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}
