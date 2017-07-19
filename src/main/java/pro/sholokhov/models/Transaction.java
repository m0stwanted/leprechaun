package pro.sholokhov.models;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class Transaction {

  private Long id;
  private DateTime date;
  private Long from;
  private Long to;
  private BigDecimal amount;

  public Transaction(Long id, DateTime date, Long from, Long to, Double amount) {
    this.id = id;
    this.date = date;
    this.from = from;
    this.to = to;
    this.amount = BigDecimal.valueOf(amount);
  }

}
