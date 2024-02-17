package ru.nsu.zhdanov.lab_3.game_context;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

public class MapCell {
  private @Getter ContextID ID;

  MapCell(ContextID ID) {
    this.ID = ID;
  }

  public boolean getCollision(Entity ent) {
    return false;
  }
}
