package ru.nsu.zhdanov.lab_3.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.interfaces.WeaponImpl;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
  protected @Getter
  @Setter int x;
  protected @Getter
  @Setter int y;
  protected @Getter
  @Setter int radius;
  protected @Getter double sinDir = 0;
  protected @Getter double cosDir = 0;
  protected @Getter int livesQuantity;
  protected @Getter
  @Setter int xShift;
  protected @Getter
  @Setter int yShift;
  protected AtomicInteger contextTracker;
  protected @Getter ContextID ID;
  protected @Getter int reward;
  protected @Getter Fraction fraction;

  public abstract void update(final GameEngine context);

  public Entity(int x, int y, int radius, int xShift,
                int yShift, int reward, int livesQuantity, ContextID ID, Fraction fraction) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.livesQuantity = livesQuantity;
    this.xShift = xShift;
    this.yShift = yShift;
    this.ID = ID;
    this.reward = reward;
    this.fraction = fraction;
  }

  public abstract void checkCollisions(final GameEngine context);

  public boolean isCollision(Entity other) {
    return Math.hypot(x - other.x, y - other.y) < radius + other.radius;
  }

  public boolean acceptDamage(WeaponImpl ent) {
    livesQuantity -= ent.getDamage();
    return livesQuantity < 0;
  }

  public boolean isDead() {
    return livesQuantity <= 0;
  }


  /**
   * decrement context tracker and make them null
   */
  public void shutdown() {
    if (Objects.isNull(contextTracker)) {
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