package ru.nsu.zhdanov.lab_3.model;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.interfaces.CollisionInterface;

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
