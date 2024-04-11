package ru.nsu.zhdanov.lab_4.model.exceptions;

public class SparePartFactoryReinstanceException extends FactoryException{
  public SparePartFactoryReinstanceException() {
    super("factory already instanced");
  }
}
