package client.exception;

public class RuntimeClientException extends RuntimeException {
  public RuntimeClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public RuntimeClientException(String message) {
    super(message);
  }
}
