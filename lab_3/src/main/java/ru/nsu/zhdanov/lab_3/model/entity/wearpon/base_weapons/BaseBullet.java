package ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.player.Player;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.interfaces.WeaponImpl;

public class BaseBullet extends Entity implements WeaponImpl {
  protected final int damage;
  protected boolean ableToUse = true;
  protected @Getter Fraction fraction;

  public BaseBullet(ContextID ID, Fraction fraction, int x, int y, double cos, double sin, double shift,
                    int damage, int livesQuantity, int radius) {
    super(x, y, radius, (int) (shift * cos), (int) (shift * sin), 0, 1, ID, fraction);
    this.damage = damage;
    this.livesQuantity = livesQuantity;
    this.fraction = fraction;
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
      if (this != ent && ent.isCollision(this)
              && ableToUse
              && (fraction != ent.getFraction() || fraction == Fraction.NON_FRACTION)) {
        ent.acceptDamage(this);
        return;
      }
    }
    Player pl =context.getPlayer();
    if (pl.isCollision(this) && ableToUse && (fraction != pl.getFraction() || fraction == Fraction.NON_FRACTION)) {
      pl.acceptDamage(this);
    }
  }

  @Override
  public int getDamage() {
    ableToUse = false;
    return damage;
  }

  @Override
  public boolean isDead() {
    return livesQuantity == 0 || !ableToUse;
  }
}
