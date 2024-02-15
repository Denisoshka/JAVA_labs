package ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader;

public class ResourceException extends RuntimeException {
  public ResourceException(String message) {
    super("command.properties are not available " + message);
  }
}
