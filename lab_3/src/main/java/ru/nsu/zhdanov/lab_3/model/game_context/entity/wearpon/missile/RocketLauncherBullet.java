package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Constants;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.BaseBullet;

public class RocketLauncherBullet extends BaseBullet implements Constants.RocketLauncherBulletC {
  //  todo uset may be deleted
  private boolean wantToDetonate;

  public RocketLauncherBullet(Fraction fraction, int x, int y, double cos, double sin) {
    super(ContextID.RocketLauncherBullet, fraction, x, y, cos, sin, SHIFT, DAMAGE, LIVES_QUANTITY, RADIUS);
  }

  @Override
  public void checkCollisions(GameContext context) {
    Entity target = null;
    double newDest;
    double dest = TRACKING_RADIUS;
//todo refactor on context type

    PlayerController pl = context.getPlayer();
    {
      for (Entity ent : context.getEntities()) {
        if (ent.getType() == ContextType.EntityT && this != ent
                && type != ent.getType() && fraction != ent.getFraction()
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
