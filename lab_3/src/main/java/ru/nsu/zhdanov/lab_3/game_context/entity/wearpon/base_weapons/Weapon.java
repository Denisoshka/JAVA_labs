package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;

public class Weapon {
  private @Getter String sprite;
  private @Getter int damage;

  public Weapon(String sprite, int damage) {
    this.sprite = sprite;
    this.damage = damage;
  }

  public boolean action(final GameContext context) {
    return false;
  }

}
