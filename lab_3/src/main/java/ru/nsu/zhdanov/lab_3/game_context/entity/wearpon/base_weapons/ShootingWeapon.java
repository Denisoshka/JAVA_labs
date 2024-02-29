package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

public abstract class ShootingWeapon extends Weapon {
  private @Getter int capacity;
  private @Getter int occupancy;

  public ShootingWeapon(ContextID ID, int damage, int occupancy) {
    super(ID, damage);
    this.occupancy = occupancy;
    this.capacity = 0;
  }
}
