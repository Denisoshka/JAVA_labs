package ru.nsu.zhdanov.lab_3.model.entity_factory;

import ru.nsu.zhdanov.lab_3.model.entity.Entity;

public interface EntityCreateInterface {
  abstract Entity create(final String command);
}
