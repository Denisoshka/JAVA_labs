package javachar.server.exceptions;

public class RuntimeServerException extends RuntimeException{
  public RuntimeServerException(String message, Throwable cause) {
    super(message, cause);
  }
}
