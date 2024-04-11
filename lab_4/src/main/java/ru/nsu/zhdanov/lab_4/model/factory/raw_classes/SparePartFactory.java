package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.exceptions.SparePartCreateException;
import ru.nsu.zhdanov.lab_4.model.exceptions.SparePartFactoryReinstanceException;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

@Slf4j
public class SparePartFactory implements AutoCloseable {
  @Getter
  private static SparePartFactory instance;
  private final Properties commandsProperties;

  private SparePartFactory(Properties properties) {
    log.debug("init SparePartFactory" + properties);
    this.commandsProperties = properties;
  }

  public SparePart newInstance(final String command) throws SparePartCreateException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    return (SparePart) Class.forName(commandsProperties.getProperty(command))
            .getDeclaredConstructor()
            .newInstance();
    /*catch (Exception e) {

      throw new SparePartCreateException(className + e.getMessage());
    }*/
  }

  public static void Instance(final Properties properties) {
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