package ru.nsu.zhdanov.lab_3.game_context;

import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

import java.util.Map;
import java.util.TreeMap;

public class GameMap {
  protected final TreeMap<Integer, TreeMap<Integer, MapCell>> map = new TreeMap<>();
  protected double maxX;
  protected double maxY;
  protected double minX;
  protected double minY;


  public boolean ableToMove(Double x, Double y, Entity ent) {
    x = Math.clamp(x, minX, maxX);
    y = Math.clamp(y, minY, maxY);
    TreeMap<Integer, MapCell> row = map.get(x.intValue());
    if (row != null) {
      MapCell cell = row.get(y.intValue());
      if (cell != null) {
//        todo
        return false;
      }
    }
    return true;
  }
}
