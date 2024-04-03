package javachar.server.exceptions;

public class ServerExceptions extends RuntimeException{
  public ServerExceptions(String message, Throwable cause) {
    super(message, cause);
  }
}
