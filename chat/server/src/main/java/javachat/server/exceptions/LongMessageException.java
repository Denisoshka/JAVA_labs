package javachat.server.exceptions;

public class LongMessageException extends RuntimeServerException{
  public LongMessageException() {
    super("Unsupported message length");
  }
}
