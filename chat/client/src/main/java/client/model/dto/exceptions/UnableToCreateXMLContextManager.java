package client.model.dto.exceptions;

public class UnableToCreateXMLContextManager extends RuntimeException {
  public UnableToCreateXMLContextManager(Throwable cause) {
    super(cause);
  }

  public UnableToCreateXMLContextManager(String message) {
    super(message);
  }
}
