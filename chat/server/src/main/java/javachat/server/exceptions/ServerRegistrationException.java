package javachat.server.exceptions;

public class ServerRegistrationException extends RuntimeServerException {
/*
  public ServerRegistrationException(String message, Throwable cause) {
    super(message, cause);
  }
*/

  public ServerRegistrationException(String message) {
    super(message);
  }
}
