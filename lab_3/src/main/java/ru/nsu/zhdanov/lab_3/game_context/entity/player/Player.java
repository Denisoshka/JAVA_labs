package ru.nsu.zhdanov.lab_3.game_context.entity.player;

import ru.nsu.zhdanov.lab_3.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.Weapon;

import java.util.Map;
import java.util.TreeMap;

public class Player extends Entity {
  static private double HITBOXRADIUS = 0.5;
  final private Map<String, Weapon> Guns;
  private Map.Entry<String, Weapon> curWeapon;

  public Player(int x, int y, int livesQuantity, int angle, final String sprite) {
    super(x, y, HITBOXRADIUS, livesQuantity, angle, sprite);
    this.Guns = new TreeMap<>();
  }

  public void acceptWeapon(final String name, final Weapon weapon) {
    Guns.put(name, weapon);
  }

  @Override
  public boolean update(final GameContext context) {
//    todo
    return false;
  }

  @Override
  public boolean checkCollisions(final GameContext context) {
//  todo
    return false;
  }
}