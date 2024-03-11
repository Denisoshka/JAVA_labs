package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.interfaces.WeaponImpl;

public class BaseBullet extends Entity implements WeaponImpl {
  protected final int damage;
  protected boolean ableToUse = true;
  protected Fraction fraction;

  public BaseBullet(ContextID ID, Fraction fraction, int x, int y, double cos, double sin, double shift,
                    int damage, int livesQuantity, int radius) {
    super(x, y, radius, (int) (shift * cos), (int) (shift * sin), 0, 1, ContextType.BulletT, ID, fraction);
    this.damage = damage;
    this.livesQuantity = livesQuantity;
    this.fraction = fraction;
  }

  @Override
  public void update(GameContext context) {
    int yShift = context.getMap().getAllowedYShift(this);
    int xShift = context.getMap().getAllowedXShift(this);
    if (yShift != this.yShift || xShift != this.xShift) {
      livesQuantity = 0;
    }
    this.x += xShift;
    this.y += yShift;
  }

  @Override
  public void checkCollisions(GameContext context) {
    for (Entity ent : context.getEntities()) {
      if (ent.getType() == ContextType.EntityT && ableToUse
              && (fraction != ent.getFraction() || fraction == Fraction.NON_FRACTION)
              && ent.isCollision(this) && this != ent) {
        ent.acceptDamage(this);
        return;
      }
    }
    PlayerController pl = context.getPlayer();
    if (ableToUse && (fraction != pl.getFraction() || fraction == Fraction.NON_FRACTION)
            && pl.isCollision(this)) {
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

  public Fraction getFraction() {
    return this.fraction;
  }
}
