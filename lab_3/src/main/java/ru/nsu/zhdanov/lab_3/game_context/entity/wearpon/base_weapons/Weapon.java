package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.WeaponImpl;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.WeaponSpriteSupplier;

public abstract class Weapon implements WeaponSpriteSupplier, WeaponImpl {
  protected ContextID ID;
  private @Getter int damage;

  public Weapon(ContextID ID, int damage) {
    this.ID = ID;
    this.damage = damage;
  }

  abstract public void action(GameEngine context, Entity ent);
}
