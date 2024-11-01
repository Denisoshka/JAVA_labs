package ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile.CycloDickFireBall;

import java.util.Random;

public class CycloDick extends Entity  {
  public static final int REWARD = 10;
  public static final int LIVES = 3000;
  public static final int RADIUS = 30;
  public static final int DEF_SHIFT = 1;
  public static final int TRACK_SHIFT = 3;
  public static final double ATTACK_DIST = 250;
  public static final double TRACK_DEGREE_DEVIATION = 10;
  public static final double DEF_DEGREE_DEVIATION = 40;


  private static final Random dirDevGen = new Random();
  private static final Random wantToShotSolver = new Random();

  private int wantToMove;
  private double dir;

  public CycloDick(int x, int y) {
    super(x, y, RADIUS, 0, 0, REWARD, LIVES, ContextType.EntityT, ContextID.CycloDick, Fraction.OPPOSITION);
  }

  @Override
  public void update(GameSession context) {
    int dx = context.getPlayer().getX() - x;
    int dy = context.getPlayer().getY() - y;
    double hyp = Math.hypot(dx, dy);

    if (hyp <= ATTACK_DIST) {
      wantToMove += wantToShotSolver.nextInt(3);
    } else {
      wantToMove += wantToShotSolver.nextInt(2);
      wantToMove -= wantToMove / 2;
    }

    dir = Math.toDegrees(Math.atan2(dy, dx));
    int shift = wantToMove > 0 ? TRACK_SHIFT : DEF_SHIFT;
    double deviation = wantToMove > 0 ? TRACK_DEGREE_DEVIATION : DEF_DEGREE_DEVIATION;
    dir += dirDevGen.nextDouble() * deviation * 2 - deviation;
    double radians = Math.toRadians(dir);
    cosDir = Math.cos(radians);
    sinDir = Math.sin(radians);

    xShift = (int) (shift * cosDir);
    yShift = (int) (shift * sinDir);

    int xfShift = context.getMap().getAllowedXShift(this);
    int yfShift = context.getMap().getAllowedYShift(this);
    int div = wantToMove > 0 ? 2 : 3;

    if (yShift != yfShift || xShift != xfShift) {
      yfShift /= div;
      xfShift /= div;
    }

    x += xfShift;
    y += yfShift;

    if (wantToShotSolver.nextInt() % 11 == 0) {
      context.submitAction(new CycloDickFireBall(x, y, cosDir, sinDir));
    }
  }

  @Override
  public void checkCollisions(GameSession context) {
  }
}
