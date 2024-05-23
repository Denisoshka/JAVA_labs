package server.exceptions;

public class ServerConfigUnavailable extends RuntimeServerException {
  public ServerConfigUnavailable(String message, Throwable cause) {
    super(STR."Config: \{message}unavailable", cause);
  }
}
