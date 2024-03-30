package ru.nsu.zhdanov.lab_3.fx_facade.exceptions;

public class FacadeException extends RuntimeException {
  public FacadeException(String message) {
    super(message);
  }

  public FacadeException(Throwable cause) {
    super(cause);
  }
}
