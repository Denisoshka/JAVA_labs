package ru.nsu.zhdanov.lab_3.model.game_context;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;

public class GameMap {
  final private @Getter ContextID ID;
  private @Getter int maxX;
  private @Getter int maxY;
  private @Getter int minX;
  private @Getter int minY;

  public GameMap(int minX, int minY, int maxX, int maxY) {
    this.maxX = maxX;
    this.maxY = maxY;
    this.minX = minX;
    this.minY = minY;
    this.ID = ContextID.Map;
  }

  public int getAllowedXShift(Entity ent) {
    return getAllowedShift(ent.getX(), ent.getXShift(), minX, maxX, ent.getRadius());
  }

  public int getAllowedYShift(Entity ent) {
    return getAllowedShift(ent.getY(), ent.getYShift(), minY, maxY, ent.getRadius());
  }

  public int getAllowedXPlacement(int point, int radius) {
    return getAllowedShift(point, maxX, minX, maxX, radius);
  }

  public int getAllowedYPlacement(int point, int radius) {
    return getAllowedShift(point, maxY, minY, maxY, radius);
  }

  private int getAllowedShift(int point, int shift, int minEdge, int maxEdge, int radius) {
    return Math.clamp(point + shift, minEdge + radius, maxEdge - radius) - point;
  }
}