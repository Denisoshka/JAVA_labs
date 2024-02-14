package ru.nsu.zhdanov.lab_3.game_context;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameContext {
  protected @Getter GameMap map;
  protected @Getter Player player;
  protected @Getter List<Entity> entities;

  GameContext(Properties mapProperties) {
  }

  public void update() {//
    player.update(this);
    for (Entity ent : entities) {
      ent.update(this);
    }

    player.checkCollisions(this);
    for (Entity ent : entities) {
      ent.checkCollisions(this);
    }

    entities.removeIf((entity) -> !entity.isAlive());
  }
}




