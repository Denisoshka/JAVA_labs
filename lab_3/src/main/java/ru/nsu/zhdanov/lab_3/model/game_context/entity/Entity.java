package ru.nsu.zhdanov.lab_3.model.game_context.entity;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.interfaces.WeaponImpl;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
  protected int x;
  protected int y;
  protected int radius;
  protected double sinDir = 0;
  protected double cosDir = 0;
  protected int livesQuantity;
  protected int xShift;
  protected int yShift;
  protected AtomicInteger contextTracker;
  protected ContextID ID;
  protected int reward;
  protected Fraction fraction;
  protected ContextType type;

  public abstract void update(final GameContext context);

  public Entity(int x, int y, int radius, int xShift,
                int yShift, int reward, int livesQuantity, ContextType type, ContextID ID, Fraction fraction) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.livesQuantity = livesQuantity;
    this.xShift = xShift;
    this.yShift = yShift;
    this.ID = ID;
    this.reward = reward;
    this.fraction = fraction;
    this.type = type;
  }

  public abstract void checkCollisions(final GameContext context);

  public boolean isCollision(Entity other) {
    return Math.hypot(x - other.x, y - other.y) < radius + other.radius;
  }

  public void acceptDamage(WeaponImpl ent) {
    livesQuantity -= ent.getDamage();
  }

  public boolean isDead() {
    return livesQuantity <= 0;
  }


  /**
   * decrement context tracker and make them null
   */
  public void shutdown() {
    if (contextTracker == null) {
      return;
    }
    contextTracker.decrementAndGet();
    contextTracker = null;
  }

  /**
   * set context tracker for track live entities and increment them
   */
  public void setContextTracker(final AtomicInteger tracker) {
    contextTracker = tracker;
    contextTracker.incrementAndGet();
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getRadius() {
    return this.radius;
  }

  public double getSinDir() {
    return this.sinDir;
  }

  public double getCosDir() {
    return this.cosDir;
  }

  public int getLivesQuantity() {
    return this.livesQuantity;
  }

  public int getXShift() {
    return this.xShift;
  }

  public int getYShift() {
    return this.yShift;
  }

  public ContextID getID() {
    return this.ID;
  }

  public int getReward() {
    return this.reward;
  }

  public Fraction getFraction() {
    return this.fraction;
  }

  public ContextType getType() {
    return this.type;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public void setXShift(int xShift) {
    this.xShift = xShift;
  }

  public void setYShift(int yShift) {
    this.yShift = yShift;
  }

/*
  public int getXCollision(final GameEngine context) {
    int floorX = Math.floorDiv(x, context.getMap().getCellSize());
    int edge = floorX * context.getMap().getCellSize() - 1; // minus for handle right collision
    if ((x + xShift) - edge < radius) {
      return edge;
    }
    edge = (floorX + 1) * context.getMap().getCellSize();
    if (edge - (x + xShift) < radius) {
      return edge;
    }
    return -1;
  }
*/

/*
  public int getYCollision(final GameEngine context) {
    int floorY = Math.floorDiv(y, context.getMap().getCellSize());
    int edge = floorY * context.getMap().getCellSize() - 1;
    if (y + yShift - edge < radius) {
      return edge;
    }
    edge = (floorY + 1) * context.getMap().getCellSize();
    if (edge - (y + yShift) < radius) {
      return edge;
    }
    return -1;
  }
*/


//  protected void handleMove(final GameEngine context) {
//    return;
//  }
/*
  protected boolean handleCollisions(final GameEngine context) {
    boolean ret = false;
    int maxX = context.getMap().getMaxX() - radius,
            minX = context.getMap().getMinX() + radius;
    int maxY = context.getMap().getMaxY() - radius,
            minY = context.getMap().getMinY() + radius;

    int fixedXShift = Math.clamp(xShift, minX - x, maxX - x);
    int fixedYShift = Math.clamp(yShift, minY - y, maxY - y);

    ret = (fixedYShift == yShift || fixedXShift == xShift);

    xShift = fixedXShift;
    yShift = fixedYShift;

    return ret;
  }*/
/*int xCell = -1;
    MapCell cellx = null;
    if (xShift != 0 && (xCell = getXCollision(context)) != -1
            && (cellx = context.getMap().getCell(xCell, y)) != null) {
      if (xCell - (x + xShift) < radius) {
        xShift = xCell - radius;
      } else {
        xShift = xCell + radius;
      }
    }

    int yCell = -1;
    MapCell celly = null;
    if (yShift != 0 && (yCell = getYCollision(context)) != -1
            && (celly = context.getMap().getCell(x, yCell)) != null) {
      if (yCell - (y + yShift) < radius) {
        yShift = yCell - radius;
      } else {
        yShift = yCell + radius;
      }
    }

    if (celly == null && celly == null && context.getMap().getCell(x + xShift, y + yShift) != null) {
      xShift = 0;
      yShift = 0;
    }*/


//   void handleAction(final GameEngine context) {
//    return;
//  }


//  @Override
//  public void drawEntitySprite(DrawInterface drawContext) {
//    return;
//  }
}
