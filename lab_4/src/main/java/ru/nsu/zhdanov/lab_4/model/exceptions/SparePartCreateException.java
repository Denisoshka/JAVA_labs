package ru.nsu.zhdanov.lab_4.model.exceptions;

public class SparePartCreateException extends FactoryException {
  public static final String message = "Unable to create command";

  public SparePartCreateException() {
    super(SparePartCreateException.message);
  }

  public SparePartCreateException(String message, Throwable cause) {
    super(SparePartCreateException.message, cause);
  }
}
