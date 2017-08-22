package pro.sholokhov.models.exceptions;

/**
 * @author Artem Sholokhov
 */
public class InactiveAccountException extends IllegalStateException {

  public InactiveAccountException() {
  }

  public InactiveAccountException(String s) {
    super(s);
  }

}
