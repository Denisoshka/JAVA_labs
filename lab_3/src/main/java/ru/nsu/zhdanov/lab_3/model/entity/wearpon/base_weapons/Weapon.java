package ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.interfaces.WeaponImpl;

public abstract class Weapon implements WeaponImpl {
  protected ContextID ID;
  private @Getter int damage;

  public Weapon(ContextID ID, int damage) {
    this.ID = ID;
    this.damage = damage;
  }

  abstract public void action(GameEngine context, Entity ent);
}
