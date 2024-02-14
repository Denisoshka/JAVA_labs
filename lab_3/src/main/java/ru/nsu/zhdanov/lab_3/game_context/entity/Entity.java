package ru.nsu.zhdanov.lab_3.game_context.entity;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.WeaponImpl;

public class Entity extends HitBox {
  protected @Getter int angle;
  protected @Getter int livesQuantity;
  protected @Getter String sprite;
  protected Double xShift = (double) 0;
  protected Double yShift = (double) 0;

  public Entity(double x, double y, double radius, int LivesQuantity, int Angle, String Sprite) {
    super(x, y, radius);
    this.livesQuantity = LivesQuantity;
    this.angle = Angle;
    this.sprite = Sprite;
  }

  public boolean update(final GameContext context) {
    return context.getMap().ableToMove(xShift, yShift, this);
  }

  public boolean checkCollisions(final GameContext context) {
    return false;
  }

  public boolean acceptDamage(WeaponImpl ent) {
    livesQuantity -= ent.getDamage();
    return livesQuantity < 0;
  }

  public boolean isAlive() {
    return livesQuantity > 0;
  }
}
