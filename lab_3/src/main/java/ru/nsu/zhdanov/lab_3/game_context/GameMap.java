package ru.nsu.zhdanov.lab_3.game_context;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

import java.util.Map;
import java.util.TreeMap;

public class GameMap {
  public int cellSize = 1000;
  protected final Map<Integer, Map<Integer, MapCell>> map;
  protected @Getter int maxX;
  protected @Getter int maxY;
  protected @Getter int minX;
  protected @Getter int minY;

  public GameMap(int minX, int minY, int maxX, int maxY, int cellSize) {
//    todo make prop
    this.maxX = maxX;
    this.maxY = maxY;
    this.minX = minX;
    this.minY = minY;
    this.cellSize = cellSize;
    this.map = new TreeMap<>();
  }

  public int getAllowedXShift(Entity ent) {
    return Math.clamp(ent.getX() + ent.getXShift(), minX + ent.getRadius(), maxX - ent.getRadius()) - ent.getX();
  }

  public int getAllowedYShift(Entity ent) {
    return Math.clamp(ent.getY() + ent.getYShift(), minX + ent.getRadius(), maxX - ent.getRadius()) - ent.getY();
  }

  public int getCellSize() {
    return cellSize;
  }

  public MapCell getCell(int x, int y) {
    x = x / cellSize;
    y = y / cellSize;
    Map<Integer, MapCell> row = map.get(x);
    if (row != null) {
      {
        return row.get(y);
      }
    }
    return null;
  }
}