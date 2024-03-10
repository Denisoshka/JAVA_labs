package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.interfaces.WeaponImpl;

public abstract class Weapon implements WeaponImpl {
  protected @Getter ContextID ID;
  private @Getter int damage;

  public Weapon(ContextID ID, int damage) {
    this.ID = ID;
    this.damage = damage;
  }

  abstract public void action(GameEngine context, Entity ent);

  abstract public boolean readyForUse();

  public void updateUse() {
  }
}
