package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.WeaponImpl;

public class BaseBullet extends Entity implements WeaponImpl {
  static String BulletSprite = "BulletSprite";
  protected int updQuantity = 5;
  protected int damage = 10;

  public BaseBullet(double x, double y, int Angle) {
    super(x, y, 0, 1, Angle, BulletSprite);
  }

  @Override
  public boolean update(GameContext context) {
    boolean updRes = false;
    for (int i = 0; i < updQuantity; ++i) {
      if (updRes = super.update(context)) {
        break;
      }
      x += xShift;
      y += yShift;
    }
    if (updRes) {
      livesQuantity = 0;
      return true;
    }
    return false;
  }

  @Override
  public boolean checkCollisions(GameContext context) {
    if (isAlive()) {
      return true;
    }
    for (Entity ent : context.getEntities()) {
      if (ent.isCollision(this) && ent.isAlive()) {
        ent.acceptDamage(this);
      }
    }
    return false;
  }

  @Override
  public int getDamage() {
//    todo maybe useless
    return damage;
  }
}
