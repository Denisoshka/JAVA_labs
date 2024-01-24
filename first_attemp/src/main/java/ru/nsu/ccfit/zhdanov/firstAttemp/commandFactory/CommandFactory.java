package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory;

import ru.nsu.ccfit.zhdanov.firstAttemp.Main;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.ClassLoaderException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.ResourceException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.Command;

import java.io.InputStream;
import java.util.Properties;

public class CommandFactory {
  private static CommandFactory instance;
  private final Properties commandsProperties;

  private CommandFactory() {
    ClassLoader classLoader;
    try {
      classLoader = Main.class.getClassLoader();
      if (classLoader == null) {
        throw new ClassLoaderException();
      }
    } catch (SecurityException e) {
      throw new ClassLoaderException();
    }

    try (InputStream in = classLoader.getResourceAsStream("command.properties")) {
      commandsProperties = new Properties();
      commandsProperties.load(in);
    } catch (Exception e) {
      throw new ResourceException();
    }
  }

  public Command create(String command){
    String className = commandsProperties.getProperty(command.toUpperCase());
    return (Command) Class.forName(className)
            .getDeclaredConstructor()
            .newInstance();

  }

  public static CommandFactory getInstance() {
    if (instance == null) {
      instance = new CommandFactory();
    }
    return instance;
  }
}
