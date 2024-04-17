package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.exceptions.SparePartCreateException;
import ru.nsu.zhdanov.lab_4.model.exceptions.SparePartFactoryReinstanceException;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

@Slf4j
public class SparePartFactory implements SparePartFactoryInterface {
  @Getter
  private final Properties commandsProperties;

  public SparePartFactory(Properties properties) {
    log.debug("init SparePartFactory" + properties);
    this.commandsProperties = properties;
  }

  public SparePart make(final String title) {
    try {
      return (SparePart) Class.forName(commandsProperties.getProperty(title))
              .getDeclaredConstructor()
              .newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
             InstantiationException | IllegalAccessException e) {
      throw new SparePartCreateException("Unable to create spare part", e);
    }
  }
}