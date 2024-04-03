package javachar.server.exceptions;

public class UnableToCreateServer extends ServerExceptions {
  public UnableToCreateServer(String message, Throwable cause) {
    super("Unable to create server on port: " + message, cause);
  }
}
