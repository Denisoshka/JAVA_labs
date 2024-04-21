package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import org.slf4j.Logger;
import ru.nsu.zhdanov.lab_4.model.exceptions.SparePartCreateException;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class SparePartFactory implements SparePartFactoryInterface {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(SparePartFactory.class);
  private final Properties commandsProperties;

  public SparePartFactory(Properties properties) {
    log.trace("init SparePartFactory" + properties);
    this.commandsProperties = properties;
  }

  public SparePart make(final String title) {
    try {
      return (SparePart) Class.forName(commandsProperties.getProperty(title))
              .getDeclaredConstructor()
              .newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
             InstantiationException | IllegalAccessException e) {
      SparePartCreateException ex = new SparePartCreateException("Unable to create spare part", e);
      log.trace("Unable to create spare part", e);
      throw ex;
    }
  }
}