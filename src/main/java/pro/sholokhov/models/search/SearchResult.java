package pro.sholokhov.models.search;

import pro.sholokhov.models.domain.Account;

import java.time.LocalDateTime;

public class SearchResult {

    private Long id;
    private Account from;
    private Account to;
    private LocalDateTime dateTime;
    private Double amount;

    //

    public SearchResult(Long id, Account from, Account to, LocalDateTime dateTime, Double amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    //


    public Long getId() {
        return id;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Double getAmount() {
        return amount;
    }

}
