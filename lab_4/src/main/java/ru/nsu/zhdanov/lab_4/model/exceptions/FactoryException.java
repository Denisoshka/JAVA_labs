package ru.nsu.zhdanov.lab_4.model.exceptions;

public class FactoryException extends RuntimeException {
  public FactoryException(String message) {
    super("Factory exception: " + message);
  }

  public FactoryException(String message, Throwable cause) {
    super("Factory exception: " + message, cause);
  }
}
