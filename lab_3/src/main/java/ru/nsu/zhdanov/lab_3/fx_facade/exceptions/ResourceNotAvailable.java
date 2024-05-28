package ru.nsu.zhdanov.lab_3.fx_facade.exceptions;

public class ResourceNotAvailable  extends FacadeException {
  public ResourceNotAvailable(String message, Throwable cause) {
    super("Resource not available: " + message, cause);
  }
}
