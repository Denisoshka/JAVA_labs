package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.WeaponImpl;

public class BaseBullet extends Entity implements WeaponImpl {
  final static int updQuantity = 5;
  final static int damage = 10;
  final static double shift = 0.3;
  protected boolean ableToUse = true;

  public BaseBullet(double x, double y, double cos, double sin) {
    super(x, y, 0, shift * cos, shift * sin, 1, ContextID.BaseBullet);
  }

  @Override
  public boolean update(GameEngine context) {
    boolean updRes = true;
    for (int i = 0; i < updQuantity; ++i) {
      if (updRes != super.update(context)) {
        livesQuantity = 0;
        break;
      }
      x += xShift;
      y += yShift;
    }
    return true;
  }

  @Override
  public boolean checkCollisions(GameEngine context) {
    if (isAlive()) {
      return true;
    }
    for (Entity ent : context.getEntities()) {
      if (ent.isCollision(this) && ableToUse) {
        if (isAlive()) {
          return true;
        }
        ent.acceptDamage(this);
      }
    }
    return false;
  }

  @Override
  public int getDamage() {
    ableToUse = false;
    return damage;
  }

  @Override
  public boolean isAlive() {
    return livesQuantity != 0 && ableToUse;
  }
}
