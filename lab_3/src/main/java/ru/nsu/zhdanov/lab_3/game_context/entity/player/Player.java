package ru.nsu.zhdanov.lab_3.game_context.entity.player;

import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.PlayerAction;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.Weapon;

import java.util.Map;
import java.util.TreeMap;

public class Player extends Entity {
  static private double HITBOXRADIUS = 0.5;
  static private double SHIFTRADIUS = 1;
  static private final double FORVARDVMOVECOEF = 1.5;
  final private Map<String, Weapon> guns;
  private Map.Entry<String, Weapon> curWeapon;

  public Player(int x, int y, int livesQuantity, int angle) {
    super(x, y, HITBOXRADIUS, 0, 0, livesQuantity, ContextID.Player);
    this.guns = new TreeMap<>();
  }

  public void acceptWeapon(final String name, final Weapon weapon) {
    guns.put(name, weapon);
  }

  @Override
  public boolean update(final GameEngine context) {
    sinDir = context.getCameraSin();
    cosDir = context.getCameraCos();
    if (containsInInput(PlayerAction.FORWARD, context)) {
      this.xShift += SHIFTRADIUS * cosDir * FORVARDVMOVECOEF;
      this.yShift += SHIFTRADIUS * sinDir * FORVARDVMOVECOEF;
    }
    if (containsInInput(PlayerAction.LEFT, context)) {
//      sin p/2 + t = cos t
//      cos p/2 + t = - sin t
      this.xShift += SHIFTRADIUS * -sinDir;
      this.yShift += SHIFTRADIUS * cosDir;
    }
    if (containsInInput(PlayerAction.RIGHT, context)) {
//      sin p/2 - t = cos t
//      cos p/2 - t = sin t
      this.xShift += SHIFTRADIUS * sinDir;
      this.yShift += SHIFTRADIUS * cosDir;
    }
    if (containsInInput(PlayerAction.RIGHT, context)) {
      this.xShift -= SHIFTRADIUS * cosDir;
      this.yShift -= SHIFTRADIUS * sinDir;
    }
    if (containsInInput(PlayerAction.SHOOT, context) && curWeapon != null) {
      curWeapon.getValue().action(context, this);
    }
//    todo finish implementation
    return false;
  }

  protected boolean containsInInput(final PlayerAction action, final GameEngine context) {
    return context.getInput().get(action).get();
  }

  @Override
  public boolean checkCollisions(final GameEngine context) {
//  todo
    return false;
  }
}