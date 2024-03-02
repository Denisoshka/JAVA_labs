package ru.nsu.zhdanov.lab_3.facade.exceptions;

public class FacadeException extends RuntimeException {
  public FacadeException(String message) {
    super(message);
  }

  public FacadeException(Throwable cause) {
    super(cause);
  }
}
