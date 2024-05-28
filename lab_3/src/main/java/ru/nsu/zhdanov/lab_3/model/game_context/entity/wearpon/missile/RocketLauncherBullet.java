package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.BaseBullet;

public class RocketLauncherBullet extends BaseBullet {
  private static final int SHIFT = 6;
  private static final int DAMAGE = 1700;
  private static final int RADIUS = 5;
  private static final int LIVES_QUANTITY = 1;
  private static final int DAMAGE_RADIUS = 50;
  private static final int TRACKING_RADIUS = 100;


  private boolean wantToDetonate;

  public RocketLauncherBullet(Fraction fraction, int x, int y, double cos, double sin) {
    super(ContextID.RocketLauncherBullet, fraction, x, y, cos, sin, SHIFT, DAMAGE, LIVES_QUANTITY, RADIUS);
  }

  @Override
  public void checkCollisions(GameSession context) {
    Entity target = null;
    double newDest;
    double dest = TRACKING_RADIUS;

    PlayerController pl = context.getPlayer();
    {
      for (Entity ent : context.getEntities()) {
        if (ent.getType() == ContextType.EntityT
                && fraction != ent.getFraction() && this != ent
                && ((newDest = Math.hypot(x - ent.getX(), y - ent.getY())) < dest)) {
          dest = newDest;
          target = ent;
          if (ent.isCollision(this)) {
            wantToDetonate = true;
          }
        }
      }
      if (fraction != pl.getFraction() && ((newDest = Math.hypot(x - pl.getX(), y - pl.getY())) < dest)) {
        dest = newDest;
        target = pl;
        pl.acceptDamage(this);
        if (pl.isCollision(this)) {
          wantToDetonate = true;
        }
      }
    }

    {
      if (wantToDetonate) {
        for (Entity ent : context.getEntities()) {
          if ((Math.hypot(x - ent.getX(), y - ent.getY()) < DAMAGE_RADIUS + ent.getRadius() + radius)) {
            ent.acceptDamage(this);
          }
        }
        if ((Math.hypot(x - pl.getX(), y - pl.getY()) < DAMAGE_RADIUS + pl.getRadius() + radius)) {
          pl.acceptDamage(this);
        }
        livesQuantity = 0;
        ableToUse = false;
        return;
      }
    }

    if (target != null) {
      int dx = target.getX() - x;
      int dy = target.getY() - y;
      xShift = (int) (SHIFT * (cosDir = dx / dest));
      yShift = (int) (SHIFT * (sinDir = dy / dest));
    }
  }
}
