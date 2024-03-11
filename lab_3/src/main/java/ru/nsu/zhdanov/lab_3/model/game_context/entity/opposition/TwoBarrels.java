package ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Constants;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons.ItIsGoingToHurtBullet;

import java.util.Random;

@Slf4j
public class TwoBarrels extends Entity implements Constants.TwoBarrelsC {
  private final static Random dirDevGen = new Random();

  private int targetX;
  private int targetY;
  private long lastShot;

  public TwoBarrels(int x, int y) {
    super(x, y, RADIUS, 0, 0, REWARD, LIVES, ContextType.EntityT, ContextID.TwoBarrels, Fraction.OPPOSITION);
  }

  @Override
  public void update(GameEngine context) {
    int dx = targetX - x;
    int dy = targetY - y;
    double dest = Math.hypot(dx, dy);

    double baseDir = Math.toDegrees(Math.atan2(dy, dx));
    if (dest < ATTACK_DIST && System.currentTimeMillis() - lastShot >= DELAY) {
      for (int i = 0; i < SHOOTS_QUANTITY; ++i) {
        double dir = Math.toRadians(baseDir + dirDevGen.nextDouble() * BULLET_DEGREE_DEVIATION_COEF - BULLET_DEGREE_DEVIATION);
        context.getActionTraceBuffer().add(new ItIsGoingToHurtBullet(fraction, x, y, Math.cos(dir), Math.sin(dir)));
        log.info("ratatata " + i);
      }
      lastShot = System.currentTimeMillis();
    }

    xShift = (int) (DEF_SHIFT * (cosDir = (dx / dest)));
    yShift = (int) (DEF_SHIFT *  (sinDir = (dy / dest)));
    int xfShift = context.getMap().getAllowedXShift(this);
    int yfShift = context.getMap().getAllowedYShift(this);

    if (xShift != xfShift || yShift != yfShift) {
      xShift /= 2;
      yShift /= 2;
    }
    x += xShift;
    y += yShift;
  }

  @Override
  public void checkCollisions(GameEngine context) {
    double dest = Double.POSITIVE_INFINITY;
    double newDest;
    for (var ent : context.getEntities()) {
      if (ent.getType() == ContextType.EntityT && !ent.isDead()
              && (fraction != ent.getFraction() || fraction == Fraction.NON_FRACTION)
              && ((newDest = Math.hypot(x - ent.getX(), y - ent.getY())) < dest)) {
        dest = newDest;
        targetX = ent.getX();
        targetY = ent.getY();
      }
    }
    Entity ent = context.getPlayer();
    if ((fraction != ent.getFraction() || fraction == Fraction.NON_FRACTION)
            && (Math.hypot(x - ent.getX(), y - ent.getY()) < dest)) {
      targetX = ent.getX();
      targetY = ent.getY();
      log.info("targetX=" + targetX + " targetY=" + targetY);
    }
  }
}
