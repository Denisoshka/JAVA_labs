package ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader;

import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.FactoryException;

public class ResourceException extends RuntimeException {
  public ResourceException(String message) {
    super("command.properties are not available " + message);
  }
}
