package javachar.server.exceptions;

import java.io.IOException;

public class IOServerException extends IOException {
  public IOServerException(String message, Throwable cause) {
    super(message, cause);
  }
}
