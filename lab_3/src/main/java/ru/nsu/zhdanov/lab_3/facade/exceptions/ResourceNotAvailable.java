package ru.nsu.zhdanov.lab_3.facade.exceptions;

public class ResourceNotAvailable  extends FacadeException {
  public ResourceNotAvailable(String message) {
    super("Resource not available: " + message);
  }

  public ResourceNotAvailable(Throwable cause) {
    super(cause);
  }
}
