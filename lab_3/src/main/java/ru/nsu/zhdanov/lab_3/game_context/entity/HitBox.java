package ru.nsu.zhdanov.lab_3.game_context.entity;

import lombok.Getter;
import lombok.Setter;

public class HitBox {
  protected @Getter
  @Setter double x;
  protected @Getter
  @Setter double y;
  protected @Getter
  @Setter double radius;

  public HitBox(double x, double y, double radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  public boolean isCollision(HitBox other) {
    return (Double.compare(Math.abs(x - other.x), radius + other.radius) < 0)
            && (Double.compare(Math.abs(y - other.y), radius + other.radius) < 0);
  }
}
