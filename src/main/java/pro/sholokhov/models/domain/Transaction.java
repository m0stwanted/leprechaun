package pro.sholokhov.models.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

  private Long id;
  private Long from;
  private Long to;
  private BigDecimal amount;
  private LocalDateTime dateTime;

  public Transaction(Long id, Long from, Long to, Double amount) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.amount = BigDecimal.valueOf(amount);
    this.dateTime = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Long getFrom() {
    return from;
  }

  public Long getTo() {
    return to;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

}
