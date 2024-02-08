package ru.nsu.zhdanov.lab_4.parts_section;

import lombok.Getter;
import ru.nsu.zhdanov.lab_4.lab_4.Application;
import ru.nsu.zhdanov.lab_4.parts_section.exceptions.ClassLoaderException;
import ru.nsu.zhdanov.lab_4.parts_section.exceptions.ResourceException;
import ru.nsu.zhdanov.lab_4.parts_section.exceptions.SparePartCreateException;
import ru.nsu.zhdanov.lab_4.parts_section.exceptions.SparePartFactoryReinstanceException;

import java.io.InputStream;
import java.util.Properties;

public class SparePartFactory implements AutoCloseable {
  @Getter
  private static SparePartFactory instance;
  private final Properties commandsProperties;

  private SparePartFactory(String properties) {
    ClassLoader classLoader;
    try {
      classLoader = Application.class.getClassLoader();
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

  public SparePart newInstance(final String command) throws SparePartCreateException {
    String className = commandsProperties.getProperty(command.toUpperCase());

    try {
      return (SparePart) Class.forName(className)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new SparePartCreateException(className + e.getMessage());
    }
  }

  public static void Instance(final String properties) {
    if (instance == null) {
      instance = new SparePartFactory(properties);
    } else {
      throw new SparePartFactoryReinstanceException();
    }
  }

  @Override
  public void close() throws Exception {
    instance = null;
  }
}