package ru.nsu.zhdanov.lab_3.game_context.entity;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.WeaponImpl;

import java.util.List;

public class Entity extends HitBox implements SpriteSupplier {
  protected @Getter double sinDir = 0;
  protected @Getter double cosDir = 0;
  protected int livesQuantity;
  protected ContextID ID;
  protected @Getter
  @Setter double xShift;
  protected @Getter
  @Setter double yShift;

  public boolean update(final GameEngine context) {
    return !context.getMap().ableToMove(this);
  }

  public Entity(double x, double y, double radius, double xShift,
                double yShift, int LivesQuantity, ContextID ID) {
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

  public boolean isAlive() {
    return livesQuantity > 0;
  }

  @Override
  public ContextID getSprite() {
    return ID;
  }
}
