package ru.nsu.zhdanov.lab_3.game_context.entity.player;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.game_context.*;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.Weapon;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.melee_weapon.Axe;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons.ItsGoingToHurt;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Player extends Entity {
  static private final int HITBOXRADIUS = 25;
  static private final int NONFORWARDMOVECOEF = 4;
  static private final int FORVARDVMOVECOEF = 5;
  static private final int MOVECOEF = 2;
  final private Map<PlayerAction, Weapon> guns;
  //  private Map.Entry<String, Weapon> curWeapon;
  Weapon weapon;

  public Player(int x, int y, int livesQuantity, int angle) {
    super(x, y, HITBOXRADIUS, 0, 0, livesQuantity, ContextID.Player);
    this.guns = new HashMap();
    this.guns.put(PlayerAction.FIRSTWEAPON, new Axe());
    this.guns.put(PlayerAction.SECONDWEAPON, new ItsGoingToHurt());
    this.weapon = guns.get(PlayerAction.FIRSTWEAPON);
  }

  @Override
  public void update(final GameEngine context) {
    int dx = context.getCursorXPos() - x;
    int dy = context.getCursorYPos() - y;
//    log.info("dx=" + dx + " dy=" + dy);
    double diag = Math.hypot(dx, dy);
    sinDir = (double) dy / diag;
    cosDir = (double) dx / diag;
//    log.info("angle="+angle);
//    log.info("sinDir=" + sinDir + " cosDir=" + cosDir);

    handleMove(context);
    handleAction(context);
//    todo finish implementation
  }

  protected void handleMove(final GameEngine context) {
    this.xShift = 0;
    this.yShift = 0;
    if (containsInInput(PlayerAction.FORWARD, context)) {
      this.yShift -= MOVECOEF;// NONFORWARDMOVECOEF;
    }
    if (containsInInput(PlayerAction.LEFT, context)) {
      this.xShift -= MOVECOEF;// NONFORWARDMOVECOEF;
    }
    if (containsInInput(PlayerAction.RIGHT, context)) {
      this.xShift += MOVECOEF;//NONFORWARDMOVECOEF;
    }
    if (containsInInput(PlayerAction.BACK, context)) {
      this.yShift += MOVECOEF;// NONFORWARDMOVECOEF;
    }
    this.yShift = context.getMap().getAllowedYShift(this);
    this.xShift = context.getMap().getAllowedXShift(this);
//    handleCollisions(context);
//    log.info("x=" + x + " y=" + y + " xShift=" + xShift + " yShift=" + yShift);
    x += xShift;
    y += yShift;
  }

  private void handleAction(GameEngine context) {
    if (containsInInput(PlayerAction.FIRSTWEAPON, context)) {
      weapon = guns.get(PlayerAction.FIRSTWEAPON);
    }
    if (containsInInput(PlayerAction.SECONDWEAPON, context)) {
      weapon = guns.get(PlayerAction.SECONDWEAPON);
    }

    if (containsInInput(PlayerAction.SHOOT, context) && weapon != null) {
      weapon.action(context, this);
      context.getInput().get(PlayerAction.SHOOT).set(false);
    }
    //    todo maybe add some other func
  }

  private boolean containsInInput(final PlayerAction action, final GameEngine context) {
    return context.getInput().get(action).get();
  }

  @Override
  public void checkCollisions(final GameEngine context) {
//  todo
  }

  @Override
  public void drawEntitySprite(DrawInterface drawContext) {
    drawContext.draw(ID, x - radius / 2, y - radius / 2, radius * 2, radius * 2, 0, 0, false);
    weapon.drawWeaponSprite(drawContext, x + radius, y + radius, 0, 0, false);
  }
}