package ru.nsu.zhdanov.lab_3.game_context.entity.opposition;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons.ItIsGoingToHurtBullet;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

import java.util.Random;

public class CycloDick extends Entity {
  private static int LIVES = 200;
  private static int RADIUS = 40;
  private static double TRACKDEGREEDEVIATION = 10;
  private static double TRACKDEGREEDEVIATIONCOEF = TRACKDEGREEDEVIATION * 2;
  private static double DEFDEGREEDEVIATION = 30;
  private static double DEFDEGREEDEVIATIONCOEF = TRACKDEGREEDEVIATION * 2;
  private static final Random dirDevGen = new Random();
  private static final Random wantToShotSolver = new Random();
  private static int WANTTOMOVEQUANTITY = 10;
  private static double ATTACKDIST = 200;
  private static int TRACKSHIFT = 3;
  private static int DEFSHIFT = 1;
  private int wantToMove;
  private double dir;

  public CycloDick(int x, int y) {
    super(x, y, RADIUS, 0, 0, LIVES, ContextID.CycloDick);
  }

  @Override
  public void update(GameEngine context) {
    int dx = context.getPlayer().getX() - x;
    int dy = context.getPlayer().getY() - y;
    double hyp = Math.hypot(dx, dy);
    if (hyp <= ATTACKDIST) {
      wantToMove = WANTTOMOVEQUANTITY;
    }

    int xfShift = 0;
    int yfShift = 0;
    if (wantToMove > 0) {
      dir = Math.toDegrees(Math.atan2(dy, dx));
      dir += dirDevGen.nextDouble() * TRACKDEGREEDEVIATIONCOEF + TRACKDEGREEDEVIATION;
      double radians = Math.toRadians(dir);
      xShift = (int) (TRACKSHIFT * (cosDir = Math.sin(radians)));
      yShift = (int) (TRACKSHIFT * (sinDir = Math.cos(radians)));
      --wantToMove;
      xfShift = context.getMap().getAllowedYShift(this);
      yfShift = context.getMap().getAllowedYShift(this);
      if (yShift != yfShift || xShift != xfShift) {
        yfShift /= 2;
        xfShift /= 2;
      }
    } else if (wantToShotSolver.nextBoolean()) {
      dir += dirDevGen.nextDouble() * DEFDEGREEDEVIATIONCOEF + DEFDEGREEDEVIATION;
      double radians = Math.toRadians(dir);
      xfShift = (int) (DEFSHIFT * (cosDir = Math.sin(radians)));
      yfShift = (int) (DEFSHIFT * (sinDir = Math.cos(radians)));
      if (yShift != yfShift || xShift != xfShift) {
        yfShift /= 3;
        xfShift /= 3;
      }
    }
    x += xfShift;
    y += yfShift;

    if (hyp <= ATTACKDIST && wantToShotSolver.nextBoolean()) {
      context.getActionTraceBuffer().add(new CycloDickFireBall(x, y, cosDir, sinDir));
    }
  }

  @Override
  public void checkCollisions(GameEngine context) {
  }

  @Override
  public void drawEntitySprite(DrawInterface drawContext) {
    drawContext.draw(ID, x - radius, y - radius, radius * 2, radius * 2, cosDir, sinDir, false);
  }
}
