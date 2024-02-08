package ru.nsu.zhdanov.lab_4.parts_section.exceptions;

public class SparePartCreateException extends FactoryException{
  public SparePartCreateException(String message) {
    super("unable to create command " + message);
  }
}
