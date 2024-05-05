package javachat.client.model.DTO.exceptions;

import java.io.IOException;

public class UnableToSerialize extends IOException {
  public UnableToSerialize(String message) {
    super(message);
  }

  public UnableToSerialize(Throwable cause) {
    super(cause);
  }
}
