package ru.nsu.zhdanov.lab_3.game_context.entity;

import lombok.Getter;
import lombok.Setter;

public class HitBox {
  protected @Getter
  @Setter int x;
  protected @Getter
  @Setter int y;
  protected @Getter
  @Setter int radius;

  public HitBox(int x, int y, int radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  public boolean isCollision(HitBox other) {
    return (Double.compare(Math.abs(x - other.x), radius + other.radius) < 0)
            && (Double.compare(Math.abs(y - other.y), radius + other.radius) < 0);
  }

}
