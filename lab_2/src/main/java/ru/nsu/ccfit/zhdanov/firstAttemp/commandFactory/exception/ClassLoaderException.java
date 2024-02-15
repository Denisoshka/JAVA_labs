package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class ClassLoaderException extends RuntimeException {
  public ClassLoaderException(final String message) {
    super("Couldn't make instance of class loader " + message);
  }
}
