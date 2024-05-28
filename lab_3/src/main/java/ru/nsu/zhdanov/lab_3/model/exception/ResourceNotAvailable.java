package ru.nsu.zhdanov.lab_3.model.exception;


public class ResourceNotAvailable  extends ModelException {
  public ResourceNotAvailable(String message, Throwable cause) {
    super("Resource not available: " + message, cause);
  }
}
