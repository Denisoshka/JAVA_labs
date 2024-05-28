package ru.nsu.zhdanov.lab_3.fx_facade.exceptions;

public class UnexpectedError extends FacadeException {
  public UnexpectedError(String message, Throwable cause) {
    super(message, cause);
  }
}
