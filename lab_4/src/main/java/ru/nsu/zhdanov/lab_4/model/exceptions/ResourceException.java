package ru.nsu.zhdanov.lab_4.model.exceptions;

public class ResourceException extends FactoryException {
  public ResourceException() {
    super("command.properties are not available");
  }
}
