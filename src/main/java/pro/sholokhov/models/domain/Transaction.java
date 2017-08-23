package pro.sholokhov.models.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pro.sholokhov.utils.CustomLocalDTDeserializer;
import pro.sholokhov.utils.CustomLocalDTSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

  private Long id;
  private Long from;
  private Long to;
  private BigDecimal amount;

  @JsonSerialize(using = CustomLocalDTSerializer.class)
  @JsonDeserialize(using = CustomLocalDTDeserializer.class)
  private LocalDateTime dateTime;

  public Transaction() {
    // jackson specific
  }

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

  public void setId(Long id) {
    this.id = id;
  }

  public Long getFrom() {
    return from;
  }

  public void setFrom(Long from) {
    this.from = from;
  }

  public Long getTo() {
    return to;
  }

  public void setTo(Long to) {
    this.to = to;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

}
