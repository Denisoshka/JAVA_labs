package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;

public abstract class ShootingWeapon extends Weapon {
  final private int capacity;
  protected int occupancy;

  public ShootingWeapon(ContextID ID, int damage, int capacity) {
    super(ID, damage);
    this.occupancy = capacity;
    this.capacity = capacity;
  }

  public int getCapacity() {
    return this.capacity;
  }

  public int getOccupancy() {
    return this.occupancy;
  }
}
