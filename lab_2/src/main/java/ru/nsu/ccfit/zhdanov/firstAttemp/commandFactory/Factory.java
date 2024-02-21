package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory;

import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.UnableToCreateCommand;

import java.util.Properties;

public class Factory<T> {
  private final Properties properties;

  public Factory(final Properties properties) {
    this.properties = properties;
  }

  public T create(final String command) throws UnableToCreateCommand {
    String className = properties.getProperty(command);
    try {
      return (T) Class.forName(className)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new UnableToCreateCommand(className + e.getMessage());
    }
  }
}
