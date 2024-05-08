package javachat.server.exceptions;

import java.io.IOException;

public class IOServerException extends IOException {
  public IOServerException(String message, Throwable cause) {
    super(message, cause);
  }

  public IOServerException(String message) {
    super(message);
  }
}