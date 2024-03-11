package ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;

import java.util.Random;

import static ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Constants.CycloDickC;

public class CycloDick extends Entity implements CycloDickC {
  private static final Random dirDevGen = new Random();
  private static final Random wantToShotSolver = new Random();

  private int wantToMove;
  private double dir;

  public CycloDick(int x, int y) {
    super(x, y, RADIUS, 0, 0, REWARD, LIVES, ContextType.EntityT, ContextID.CycloDick, Fraction.OPPOSITION);
  }

  @Override
  public void update(GameContext context) {
    int dx = context.getPlayer().getX() - x;
    int dy = y - context.getPlayer().getY();
    double hyp = Math.hypot(dx, dy);

    if (hyp <= ATTACK_DIST) {
      wantToMove += wantToShotSolver.nextInt(3);
    }

    int xfShift = 0;
    int yfShift = 0;
    if (wantToMove > 0) {
      dir = Math.toDegrees(Math.atan2(dy, dx));
      /* make deviation in range */
      dir += dirDevGen.nextDouble() * TRACK_DEGREE_DEVIATION_COEF - TRACK_DEGREE_DEVIATION;
      double radians = Math.toRadians(dir);
      xShift = (int) (TRACK_SHIFT * (cosDir = Math.sin(radians)));
      yShift = (int) (TRACK_SHIFT * (sinDir = Math.cos(radians)));
      --wantToMove;
      xfShift = context.getMap().getAllowedXShift(this);
      yfShift = context.getMap().getAllowedYShift(this);
      if (yShift != yfShift || xShift != xfShift) {
        yfShift /= 2;
        xfShift /= 2;
      }
    } else if (wantToShotSolver.nextBoolean()) {
      dir += dirDevGen.nextDouble() * DEF_DEGREE_DEVIATION_COEF - DEF_DEGREE_DEVIATION;
      double radians = Math.toRadians(dir);
      xShift = (int) (DEF_SHIFT * (cosDir = Math.sin(radians)));
      yShift = (int) (DEF_SHIFT * (sinDir = Math.cos(radians)));
      xfShift = context.getMap().getAllowedXShift(this);
      yfShift = context.getMap().getAllowedYShift(this);
      if (yShift != yfShift || xShift != xfShift) {
        yfShift /= 3;
        xfShift /= 3;
      }
    }
    x += xfShift;
    y += yfShift;

    if (hyp <= ATTACK_DIST && wantToShotSolver.nextInt() % 11 == 0) {
      context.submitAction(new CycloDickFireBall(x, y, cosDir, sinDir));
    }
  }

  @Override
  public void checkCollisions(GameContext context) {
  }
}
