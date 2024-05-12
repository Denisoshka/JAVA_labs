package client.exception;

import java.io.IOException;

public class IOClientException extends IOException {
  public IOClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public IOClientException(String message) {
    super(message);
  }
}
