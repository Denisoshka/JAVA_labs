package javachat.server.exceptions;

public class RuntimeServerException extends RuntimeException{
  public RuntimeServerException(String message, Throwable cause) {
    super(message, cause);
  }
  public RuntimeServerException(String message) {
    super(message);
  }
}
