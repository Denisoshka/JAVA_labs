package ru.nsu.zhdanov.lab_3.model.interfaces;

import ru.nsu.zhdanov.lab_3.model.entity.Entity;

public interface CollisionInterface {
  abstract boolean traceCollision(Entity ent);
}
