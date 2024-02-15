package ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader;

public class ClassLoaderException extends RuntimeException {
  public ClassLoaderException(final String message) {
    super("Couldn't make instance of class loader " + message);
  }
}
