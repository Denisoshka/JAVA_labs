package ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertiesLoader {
  public static Properties load(String propertiesPath) {
    Properties properties;
    try (InputStream in =
                 Objects.requireNonNull(PropertiesLoader.class.getClassLoader()).getResourceAsStream(propertiesPath)) {
      properties = new Properties();
      properties.load(in);
    } catch (Exception e) {
      throw new PropertiesLoaderException(e.getMessage());
    }
    return properties;
  }
}
