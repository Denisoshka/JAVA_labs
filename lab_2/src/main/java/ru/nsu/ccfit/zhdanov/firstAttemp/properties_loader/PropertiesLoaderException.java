package ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader;

public class PropertiesLoaderException extends RuntimeException {
  public PropertiesLoaderException(final String desc) {
    super("Unable to load properties" + desc);
  }
}
