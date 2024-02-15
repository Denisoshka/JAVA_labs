package ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader;

import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.ClassLoaderException;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
  public static Properties load(Object classObject, String propertiesPath) {
    ClassLoader classLoader;
    try {
      if ((classLoader = classObject.getClass().getClassLoader()) == null) {
        throw new ClassLoaderException(null);
      }
    } catch (Exception e) {
      throw new ClassLoaderException(e.getMessage());
    }

    Properties properties;
    try (InputStream in = classLoader.getResourceAsStream(propertiesPath)) {
      properties = new Properties();
      properties.load(in);
    } catch (Exception e) {
      throw new ResourceException(e.getMessage());
    }
    return properties;
  }
}
