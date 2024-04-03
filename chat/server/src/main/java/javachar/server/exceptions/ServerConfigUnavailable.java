package javachar.server.exceptions;

public class ServerConfigUnavailable extends ServerExceptions {
  public ServerConfigUnavailable(String message, Throwable cause) {
    super("Config: " + message + "unavailable" , cause);
  }
}
