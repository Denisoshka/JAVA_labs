package ru.nsu.zhdanov.lab_3.model.entity_factory;

import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity_factory.exception.UnableToCreateEntity;

import java.util.Properties;

public class EntityFactory implements EntityCreateInterface {
  private final Properties properties;

  public EntityFactory(final Properties properties) {
    this.properties = properties;
  }

  @Override
  public Entity create(final String command) throws UnableToCreateEntity {
    String className = properties.getProperty(command);
    try {
      return (Entity) Class.forName(className)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new UnableToCreateEntity(className + e.getMessage());
    }
  }
}
