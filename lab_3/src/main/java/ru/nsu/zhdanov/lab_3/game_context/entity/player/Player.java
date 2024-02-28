package ru.nsu.zhdanov.lab_3.game_context.entity.player;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.game_context.*;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.Weapon;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class Player extends Entity {
  static private final int HITBOXRADIUS = 50;
  static private final int SHIFTRADIUS = 1;
  static private final int FORVARDVMOVECOEF = 150;
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

    handleMove(context);
    handleAction(context);
//    todo finish implementation
    return false;
  }

  @Override
  protected void handleMove(final GameEngine context) {
    this.xShift = 0;
    this.yShift = 0;
    if (containsInInput(PlayerAction.FORWARD, context)) {
      this.xShift += (int) (SHIFTRADIUS * cosDir * FORVARDVMOVECOEF);
      this.yShift += (int) (SHIFTRADIUS * sinDir * FORVARDVMOVECOEF);
    }
    if (containsInInput(PlayerAction.LEFT, context)) {
      /* sin p/2 + t = cos t   */
      /* cos p/2 + t = - sin t */
      this.xShift += (int) (SHIFTRADIUS * -sinDir);
      this.yShift += (int) (SHIFTRADIUS * cosDir);
    }
    if (containsInInput(PlayerAction.RIGHT, context)) {
      /* sin p/2 - t = cos t   */
      /* cos p/2 - t = sin t   */
      this.xShift += (int) (SHIFTRADIUS * sinDir);
      this.yShift += (int) (SHIFTRADIUS * cosDir);
    }
    if (containsInInput(PlayerAction.BACK, context)) {
      this.xShift -= (int) (SHIFTRADIUS * cosDir);
      this.yShift -= (int) (SHIFTRADIUS * sinDir);
    }
    this.yShift = context.getMap().getAllowedYShift(this);
    this.xShift = context.getMap().getAllowedXShift(this);
    handleCollisions(context);
  }

  @Override
  protected void handleAction(GameEngine context) {
    if (containsInInput(PlayerAction.SHOOT, context) && curWeapon != null) {
      curWeapon.getValue().action(context, this);
    }
    handleMove(context);
//    todo maybe add some other func
  }

  private boolean containsInInput(final PlayerAction action, final GameEngine context) {
    return context.getInput().get(action).get();
  }

  @Override
  public boolean checkCollisions(final GameEngine context) {
//  todo
    return false;
  }

  @Override
  public void drawSprite(DrawInterface drawContext) {
    drawContext.draw(ID, x - radius, y - radius, radius * 2, radius * 2, 0, 0);
    curWeapon.getValue().drawSprite(drawContext);
  }
}