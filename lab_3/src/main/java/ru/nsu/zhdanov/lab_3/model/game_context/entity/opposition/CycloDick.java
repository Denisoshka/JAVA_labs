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
    double devDeviation = wantToMove > 0 ? TRACK_DEGREE_DEVIATION : DEF_DEGREE_DEVIATION;
    dir += dirDevGen.nextDouble() * devDeviation * 2 - devDeviation;
    double radians = Math.toRadians(dir);
    xShift = (int) (shift * (cosDir = Math.cos(radians)));
    yShift = (int) (shift * (sinDir = Math.sin(radians)));

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
  public void checkCollisions(GameContext context) {
  }
}
