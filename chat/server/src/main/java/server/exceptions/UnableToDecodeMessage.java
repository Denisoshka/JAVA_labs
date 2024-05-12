package server.exceptions;

public class UnableToDecodeMessage extends RuntimeServerException {
  public UnableToDecodeMessage(String message, Throwable cause) {
    super(message, cause);
  }
}
