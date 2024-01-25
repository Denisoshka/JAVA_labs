package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory;

import lombok.Getter;
import ru.nsu.ccfit.zhdanov.firstAttemp.Main;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.CommandCreateException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.ClassLoaderException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.FactoryReinstanceException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.ResourceException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.Command;

import java.io.InputStream;
import java.util.Properties;

public class CommandFactory implements AutoCloseable {
  @Getter
  private static CommandFactory instance;
  private final Properties commandsProperties;

  private CommandFactory(String properties) {
    ClassLoader classLoader;
    try {
      classLoader = Main.class.getClassLoader();
      if (classLoader == null) {
        throw new ClassLoaderException();
      }
    } catch (SecurityException e) {
      throw new ClassLoaderException();
    }

    try (InputStream in = classLoader.getResourceAsStream(properties)) {
      commandsProperties = new Properties();
      commandsProperties.load(in);
    } catch (Exception e) {
      throw new ResourceException();
    }
  }

  public Command create(final String command) /*throws ClassNotFoundException,
          NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException*/ {
    String className = commandsProperties.getProperty(command.toUpperCase());

    try {
      return (Command) Class.forName(className)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new CommandCreateException(className + e.getMessage());
    }
  }

  public static void instance(final String properties) {
    if (instance == null) {
      instance = new CommandFactory(properties);
    } else {
      throw new FactoryReinstanceException();
    }
  }

  @Override
  public void close() throws Exception {
    instance = null;
  }
}
