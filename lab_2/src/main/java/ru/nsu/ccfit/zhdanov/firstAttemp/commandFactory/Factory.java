package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory;

import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.CommandCreateException;

import java.util.Properties;

public class Factory<T> {
  private final Properties properties;

  public Factory(final Properties properties) {
    this.properties = properties;
  }

  public T create(final String command) throws CommandCreateException {
    String className = properties.getProperty(command.toUpperCase());
    try {
      return (T) Class.forName(className)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new CommandCreateException(className + e.getMessage());
    }
  }
}
