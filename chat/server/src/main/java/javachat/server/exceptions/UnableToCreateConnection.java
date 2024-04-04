package javachat.server.exceptions;

public class UnableToCreateConnection extends IOServerException {
  public UnableToCreateConnection(String message, Throwable cause) {
    super(message, cause);
  }
}
