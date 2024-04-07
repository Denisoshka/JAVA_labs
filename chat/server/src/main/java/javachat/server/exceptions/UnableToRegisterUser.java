package javachat.server.exceptions;

public class UnableToRegisterUser extends RuntimeServerException {
  public UnableToRegisterUser(String message) {
    super(message);
  }
}
