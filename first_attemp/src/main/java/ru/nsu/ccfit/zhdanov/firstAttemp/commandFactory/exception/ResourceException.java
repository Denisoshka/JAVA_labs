package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class ResourceException extends FactoryException {
  public ResourceException() {
    super("command.properties are not available");
  }
}
