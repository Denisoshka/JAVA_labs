package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class ClassLoaderException extends FactoryException {
  public ClassLoaderException(final String message) {
    super("Couldn't make instance of factory class loader");
  }
}
