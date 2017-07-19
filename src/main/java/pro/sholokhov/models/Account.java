package pro.sholokhov.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Account {

  private Long id;
  private String name;
  private AtomicBoolean active;
  private AtomicReference<BigDecimal> balance;

  @JsonIgnore
  private final Object mutex = new Object();

  public Account(Long id, String name, Double balance) {
    this.id = id;
    this.name = name;
    this.active = new AtomicBoolean(true);
    this.balance = new AtomicReference<>(BigDecimal.valueOf(balance));
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
    return balance.get();
  }

  public boolean isActive() {
    return active.get();
  }

  public Object getMutex() {
    return mutex;
  }

  //

  public void deactivate() {
    active.set(false);
  }

  public void updateBalance(BigDecimal amount) {
    balance.updateAndGet(b -> b.add(amount));
  }

}
