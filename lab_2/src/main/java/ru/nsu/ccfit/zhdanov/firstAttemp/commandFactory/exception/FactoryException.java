package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class FactoryException extends RuntimeException {
  public FactoryException(final String message) {
    super("Factory exception: " + message);
  }
}
