package ru.nsu.zhdanov.lab_3.game_context.interfaces;

import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

public interface CollisionInterface {
  abstract boolean traceCollision(Entity ent);
}
