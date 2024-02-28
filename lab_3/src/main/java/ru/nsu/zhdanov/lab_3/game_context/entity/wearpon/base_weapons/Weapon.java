package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.SpriteSupplier;

public class Weapon implements SpriteSupplier {
  private ContextID ID;
  private @Getter int damage;

  public Weapon(ContextID ID, int damage) {
    this.ID = ID;
    this.damage = damage;
  }

  public boolean action(final GameEngine context, final Entity user) {
    return false;
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