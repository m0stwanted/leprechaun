package pro.sholokhov.models;

import java.math.BigDecimal;

public class Account {

  private Long id;
  private String name;
  private BigDecimal balance;
  private Boolean active;

  public Account(Long id, String name, Double balance) {
    this.id = id;
    this.name = name;
    this.balance = BigDecimal.valueOf(balance);
    this.active = true;
  }

  // getters
  //

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public boolean isActive() {
    return active;
  }

  //

  public void deactivate() {
    active = false;
  }

}
