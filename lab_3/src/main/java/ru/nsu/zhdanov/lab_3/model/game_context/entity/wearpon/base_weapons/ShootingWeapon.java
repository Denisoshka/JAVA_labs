package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;

public abstract class ShootingWeapon extends Weapon {
  @Getter final private  int capacity;
  @Getter protected int occupancy;

  public ShootingWeapon(ContextID ID, int damage, int capacity) {
    super(ID, damage);
    this.occupancy = capacity;
    this.capacity = capacity;
  }

  @Override
  public void updateUse() {
    occupancy = capacity;
  }
}
