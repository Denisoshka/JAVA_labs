package ru.nsu.zhdanov.lab_3.model;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;

public class GameMap {
  public int cellSize = 1000;
//  protected final Map<Integer, Map<Integer, MapCell>> map;
  final private @Getter ContextID ID;
  private @Getter int maxX;
  private @Getter int maxY;
  private @Getter int minX;
  private @Getter int minY;

  public GameMap(int minX, int minY, int maxX, int maxY, int cellSize) {
//    todo make prop
    this.maxX = maxX;
    this.maxY = maxY;
    this.minX = minX;
    this.minY = minY;
    this.ID = ContextID.Map;
//    this.map = new TreeMap<>();
  }

  public int getAllowedXShift(Entity ent) {
    return Math.clamp(ent.getX() + ent.getXShift(), minX + ent.getRadius(), maxX - ent.getRadius()) - ent.getX();
  }

  public int getAllowedYShift(Entity ent) {
    return Math.clamp(ent.getY() + ent.getYShift(), minX + ent.getRadius(), maxX - ent.getRadius()) - ent.getY();
  }
}