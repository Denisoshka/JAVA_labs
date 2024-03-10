package ru.nsu.zhdanov.lab_3.model.game_context.entity_factory;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;

public interface EntityCreateInterface {
  abstract Entity create(final String command);
}
