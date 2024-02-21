package ru.nsu.zhdanov.lab_3.game_context.entity;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.GameMap;
import ru.nsu.zhdanov.lab_3.game_context.MapCell;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.SpriteSupplier;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.WeaponImpl;

public class Entity extends HitBox implements SpriteSupplier {
  protected @Getter double sinDir = 0;
  protected @Getter double cosDir = 0;
  protected int livesQuantity;
  protected ContextID ID;
  protected @Getter
  @Setter int xShift;
  protected @Getter
  @Setter int yShift;

  public boolean update(final GameEngine context) {
    return false;
  }

  public Entity(int x, int y, int radius, int xShift,
                int yShift, int LivesQuantity, ContextID ID) {
    super(x, y, radius);
    this.livesQuantity = LivesQuantity;
    this.xShift = xShift;
    this.yShift = yShift;
    this.ID = ID;
  }

  public boolean checkCollisions(final GameEngine context) {
    return false;
  }

  public boolean acceptDamage(WeaponImpl ent) {
    livesQuantity -= ent.getDamage();
    return livesQuantity < 0;
  }

  public int getXCollision(final GameEngine context) {
    int floorX = Math.floorDiv(x, context.getMap().getCellSize());
    int edge = floorX * context.getMap().getCellSize() - 1;
    if ((x + xShift) - edge < radius) {
      return edge;
    }
    edge = (floorX + 1) * context.getMap().getCellSize();
    if (edge - (x + xShift) < radius) {
      return edge;
    }
    return -1;
  }

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


  protected void handleMove(final GameEngine context) {
    return;
  }

  protected void handleCollisions(final GameEngine context) {
    int xCell = -1;
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
    }
  }

  protected void handleAction(final GameEngine context) {
    return;
  }


  public boolean isAlive() {
    return livesQuantity > 0;
  }

  @Override
  public ContextID getSprite() {
    return ID;
  }

  @Override
  public void drawSprite(DrawInterface drawContext) {
    return;
  }
}
