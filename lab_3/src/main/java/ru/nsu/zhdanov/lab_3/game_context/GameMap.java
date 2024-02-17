package ru.nsu.zhdanov.lab_3.game_context;

import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

import java.util.Map;
import java.util.TreeMap;

public class GameMap {
  protected final Map<Integer, Map<Integer, MapCell>> map;
  protected int maxX;
  protected int maxY;
  protected int minX;
  protected int minY;

  public GameMap(int minX, int minY, int maxX, int maxY) {
//    todo make prop
    this.maxX = maxX;
    this.maxY = maxY;
    this.minX = minX;
    this.minY = minY;
    this.map = new TreeMap<>();
  }

  public boolean ableToMove(Entity ent) {
    boolean ret = true;
    double x = Math.clamp(ent.getX() + ent.getXShift(), minX, maxX);
    double y = Math.clamp(ent.getY() + ent.getYShift(), minX, maxX);
    if (Double.compare(x, ent.getX()) != 0 || Double.compare(y, ent.getY()) != 0) {
      ret = false;
    }
    ent.setXShift(x);
    ent.setYShift(y);

    Map<Integer, MapCell> row = map.get((int) ent.getXShift());
    if (row != null) {
      MapCell cell = row.get((int) ent.getYShift());
      if (cell != null) {
        return cell.getCollision(ent);
      }
    }
    return ret;
  }
}
