package pro.sholokhov.models;

public class Account {

  private Long id;
  private String name;
  private Double balance;

  public Account(Long id, String name, Double balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getBalance() {
    return balance;
  }

}
