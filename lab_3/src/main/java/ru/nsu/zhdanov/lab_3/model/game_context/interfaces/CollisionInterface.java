package ru.nsu.zhdanov.lab_3.model.game_context.interfaces;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;

public interface CollisionInterface {
  abstract boolean traceCollision(Entity ent);
}
