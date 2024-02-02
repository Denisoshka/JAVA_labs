package ru.nsu.zhdanov.lab4.parts_section.exceptions;

public class SparePartFactoryReinstanceException extends FactoryException{
  public SparePartFactoryReinstanceException() {
    super("factory already instanced");
  }
}
