package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.WeaponImpl;

public class BaseBullet extends Entity implements WeaponImpl {
  protected final int damage;
  protected double shift;
  protected boolean ableToUse = true;
  protected final int spriteRadius;

  public BaseBullet(ContextID ID, int x, int y, double cos, double sin, double shift,
                    int damage, int livesQuantity, int radius, int spriteRadius) {
    super(x, y, radius, (int) (shift * cos), (int) (shift * sin), 1, ID);
    this.spriteRadius = spriteRadius;
    this.damage = damage;
    this.livesQuantity = livesQuantity;
  }

  @Override
  public void update(GameEngine context) {
    int yShift = context.getMap().getAllowedYShift(this);
    int xShift = context.getMap().getAllowedXShift(this);
    if (yShift != this.yShift || xShift != this.xShift) {
      livesQuantity = 0;
    }
    this.x += xShift;
    this.y += yShift;
  }

  @Override
  public void checkCollisions(GameEngine context) {
    for (Entity ent : context.getEntities()) {
      if (ent.isCollision(this) && ableToUse) {
        ent.acceptDamage(this);
        return;
      }
    }
  }

  @Override
  public int getDamage() {
    ableToUse = false;
    return damage;
  }

  @Override
  public boolean isAlive() {
    return livesQuantity == 0 || !ableToUse;
  }

  @Override
  public void drawEntitySprite(DrawInterface drawContext) {
//    todo
    drawContext.draw(ID, x, y, 6, 6, sinDir, cosDir, false);
  }
}
