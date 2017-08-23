package pro.sholokhov.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.sholokhov.models.domain.Account;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse extends AbstractResponse {

  private Account account;

  public AccountResponse() { }

  public AccountResponse(Account account, Boolean success, String message) {
    super(success, message);
    this.account = account;
  }

  public AccountResponse(Boolean success, String message) {
    super(success, message);
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

}
