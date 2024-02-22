package ru.nsu.ccfit.zhdanov.firstAttemp.Factory;

import ru.nsu.ccfit.zhdanov.firstAttemp.Factory.exception.UnableToCreateCommand;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;

import java.util.Properties;

public class CommandFactory implements CommandCreateInterface {
  private final Properties properties;

  public CommandFactory(final Properties properties) {
    this.properties = properties;
  }

  @Override
  public Command create(final String command) throws UnableToCreateCommand {
    String className = properties.getProperty(command);
    try {
      return (Command) Class.forName(className)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new UnableToCreateCommand(className + e.getMessage());
    }
  }
}
