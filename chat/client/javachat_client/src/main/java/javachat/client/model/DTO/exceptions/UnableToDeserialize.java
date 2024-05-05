package javachat.client.model.DTO.exceptions;

import java.io.IOException;

public class UnableToDeserialize extends IOException {
  public UnableToDeserialize(String message) {
    super(message);
  }

  public UnableToDeserialize(Throwable cause) {
    super(cause);
  }
}
