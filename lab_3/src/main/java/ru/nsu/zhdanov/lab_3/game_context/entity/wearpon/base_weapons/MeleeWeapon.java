package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons;

import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.melee_weapon.AreaOfDefeat;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

public abstract class MeleeWeapon extends Weapon {
  private final @Getter int actionDistance;

  public MeleeWeapon(ContextID ID, int damage, int actionDistance) {
    super(ID, damage);
    this.actionDistance = actionDistance;
  }
  public static class AreaChecker {
    public static AreaOfDefeat checkForward = (x, y) -> x > 0;
    public static AreaOfDefeat checkBehind = (x, y) -> x < 0;
    public static AreaOfDefeat checkTop = (x, y) -> y > 0;
    public static AreaOfDefeat checkBottom = (x, y) -> y < 0;
  }
}
