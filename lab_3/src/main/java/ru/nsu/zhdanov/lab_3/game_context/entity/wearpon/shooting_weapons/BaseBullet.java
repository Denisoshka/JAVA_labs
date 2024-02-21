package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.WeaponImpl;

public class BaseBullet extends Entity implements WeaponImpl {
  final static int updQuantity = 5;
  final static int damage = 10;
  final static double shift = 100;
  protected boolean ableToUse = true;

  public BaseBullet(int x, int y, double cos, double sin) {
    super(x, y, 0, (int) (shift * cos), (int) (shift * sin), 1, ContextID.BaseBullet);
  }

  @Override
  public boolean update(GameEngine context) {
    handleMove(context);
    return true;
  }

  @Override
  protected void handleMove(GameEngine context) {
    int yShift = context.getMap().getAllowedYShift(this);
    int xShift = context.getMap().getAllowedXShift(this);
    if (yShift != this.yShift && xShift != this.xShift) {
      livesQuantity = 0;
    }
    this.yShift = yShift;
    this.xShift = xShift;
    handleCollisions(context);
    if (yShift != this.yShift && xShift != this.xShift) {
      livesQuantity = 0;
    }

    this.x += this.xShift;
    this.y += this.yShift;
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

  @Override
  public void drawSprite(DrawInterface drawContext) {
//    todo
    drawContext.draw(ID, x, y, radius, radius, sinDir, cosDir);
  }
}
