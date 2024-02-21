package ru.nsu.zhdanov.lab_3.game_context;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.CollisionInterface;

public class MapCell implements CollisionInterface {
  private @Getter ContextID ID;

  MapCell(ContextID ID) {
    this.ID = ID;
  }

  @Override
  public boolean traceCollision(Entity ent) {
    return false;
  }
}
