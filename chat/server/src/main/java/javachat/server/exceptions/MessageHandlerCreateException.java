package javachat.server.exceptions;

public class MessageHandlerCreateException extends IOServerException {
  public MessageHandlerCreateException( Throwable cause) {
    super("Unable to Create message handler", cause);
  }
}
