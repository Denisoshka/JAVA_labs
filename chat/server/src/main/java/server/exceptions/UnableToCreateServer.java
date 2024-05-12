package server.exceptions;

public class UnableToCreateServer extends RuntimeServerException {
  public UnableToCreateServer(String message, Throwable cause) {
    super("Unable to create server on port: " + message, cause);
  }
}
