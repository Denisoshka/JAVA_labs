package javachar.server.exceptions;

public class ServerConfigUnavailable extends RuntimeServerException {
  public ServerConfigUnavailable(String message, Throwable cause) {
    super("Config: " + message + "unavailable" , cause);
  }
}
