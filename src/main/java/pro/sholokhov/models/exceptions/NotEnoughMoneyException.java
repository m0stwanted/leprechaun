package pro.sholokhov.models.exceptions;

/**
 * @author Artem Sholokhov
 */
public class NotEnoughMoneyException extends IllegalStateException {

  public NotEnoughMoneyException() {
  }

  public NotEnoughMoneyException(String s) {
    super(s);
  }

}
