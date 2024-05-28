package ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile.TwoBarrelsBullet;

import java.util.Random;

public class TwoBarrels extends Entity {
  public static final int LIVES = 2500;
  public static final int RADIUS = 20;
  public static final int REWARD = 30;
  public static final int SHOOTS_QUANTITY = 6;
  public static final int DEF_SHIFT = 3;
  public static final int DELAY = 1500;
  public static final double ATTACK_DIST = 250;
  public static final double BULLET_DEGREE_DEVIATION = 10;
  public static final double BULLET_DEGREE_DEVIATION_COEF = BULLET_DEGREE_DEVIATION * 2;


  private final static Random dirDevGen = new Random();

  private int targetX;
  private int targetY;
  private long lastShot;

  public TwoBarrels(int x, int y) {
    super(x, y, RADIUS, 0, 0, REWARD, LIVES, ContextType.EntityT, ContextID.TwoBarrels, Fraction.OPPOSITION);
  }

  @Override
  public void update(GameSession context) {
    int dx = targetX - x;
    int dy = targetY - y;
    double dest = Math.hypot(dx, dy);

    double baseDir = Math.toDegrees(Math.atan2(dy, dx));
    if (dest < ATTACK_DIST && System.currentTimeMillis() - lastShot >= DELAY) {
      for (int i = 0; i < SHOOTS_QUANTITY; ++i) {
        double dir = Math.toRadians(baseDir + dirDevGen.nextDouble() * BULLET_DEGREE_DEVIATION_COEF - BULLET_DEGREE_DEVIATION);
        context.submitAction(new TwoBarrelsBullet(x, y, Math.cos(dir), Math.sin(dir)));
      }
      lastShot = System.currentTimeMillis();
    }

    xShift = (int) (DEF_SHIFT * (cosDir = (dx / dest)));
    yShift = (int) (DEF_SHIFT * (sinDir = (dy / dest)));
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
  public void checkCollisions(GameSession context) {
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
    }
  }
}
