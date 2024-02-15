package ru.nsu.zhdanov.lab_3.properties_loader;

public class ResourceException extends RuntimeException {
  public ResourceException(String message) {
    super("command.properties are not available " + message);
  }
}