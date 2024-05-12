package javachat.client.exception;

public class UnableToDecodeMessage extends RuntimeClientException {
  public UnableToDecodeMessage(String message, Throwable cause) {
    super(message, cause);
  }
}
