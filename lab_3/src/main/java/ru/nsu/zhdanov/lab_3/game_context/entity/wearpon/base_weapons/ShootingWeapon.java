package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

public class ShootingWeapon extends Weapon {
  private @Getter int capacity;
  private @Getter int occupancy;

  public ShootingWeapon(String sprite, int damage, int occupancy) {
    super(sprite, damage);
    this.occupancy = occupancy;
    this.capacity = 0;
  }

  public void reload(final Entity ent) {
    if (ent == null) {
      return;
    }
//    todo
  }
}
