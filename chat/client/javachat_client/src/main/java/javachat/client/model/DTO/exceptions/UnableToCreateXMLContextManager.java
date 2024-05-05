package javachat.client.model.DTO.exceptions;

public class UnableToCreateXMLContextManager extends RuntimeException {
  public UnableToCreateXMLContextManager(Throwable cause) {
    super(cause);
  }

  public UnableToCreateXMLContextManager(String message) {
    super(message);
  }
}
