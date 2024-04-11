package ru.nsu.zhdanov.lab_4.model.exceptions;

public class SparePartCreateException extends FactoryException{
  public SparePartCreateException(String message) {
    super("unable to create command " + message);
  }
}
