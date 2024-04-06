package javachat.server.exceptions;

public class UnableToAddConnection extends RuntimeException {
  public UnableToAddConnection(String message, Throwable cause) {
    super(message, cause);
  }
}
