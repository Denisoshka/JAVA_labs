package ru.nsu.zhdanov.lab_3.model.game_context.entity.player;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.*;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.Weapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.melee_weapon.Axe;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons.ItsGoingToHurt;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Constants.PlayerC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class Player extends Entity {
  final private Map<PlayerAction, Weapon> guns;
  @Getter
  Weapon weapon;

  public Player(int x, int y, int angle) {
    super(x, y, PlayerC.RADIUS, 0, 0, 0, PlayerC.LIVES_QUANTITY, ContextID.Player, Fraction.PLAYER);
    this.guns = new HashMap();
    this.guns.put(PlayerAction.FIRSTWEAPON, new Axe());
    this.guns.put(PlayerAction.SECONDWEAPON, new ItsGoingToHurt());
    this.weapon = guns.get(PlayerAction.FIRSTWEAPON);
  }

  @Override
  public void update(final GameEngine context) {
    int dx = context.getCursorXPos() - x;
    int dy = context.getCursorYPos() - y;
    double diag = Math.hypot(dx, dy);
    sinDir = (double) dy / diag;
    cosDir = (double) dx / diag;

    handleMove(context);
    handleAction(context);
//    todo finish implementation
  }

  protected void handleMove(final GameEngine context) {
    this.xShift = 0;
    this.yShift = 0;
    if (getFromInput(PlayerAction.FORWARD, context).get()) {
      this.yShift -= PlayerC.MOVE_COEF;// NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.LEFT, context).get()) {
      this.xShift -= PlayerC.MOVE_COEF;// NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.RIGHT, context).get()) {
      this.xShift += PlayerC.MOVE_COEF;//NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.BACK, context).get()) {
      this.yShift += PlayerC.MOVE_COEF;// NONFORWARDMOVECOEF;
    }
    this.yShift = context.getMap().getAllowedYShift(this);
    this.xShift = context.getMap().getAllowedXShift(this);
//    handleCollisions(context);
//    log.info("x=" + x + " y=" + y + " xShift=" + xShift + " yShift=" + yShift);
    x += xShift;
    y += yShift;
  }

  private void handleAction(GameEngine context) {
    AtomicBoolean act;
    if (getFromInput(PlayerAction.FIRSTWEAPON, context).get()) {
      weapon = guns.get(PlayerAction.FIRSTWEAPON);
    }
    if (getFromInput(PlayerAction.SECONDWEAPON, context).get()) {
      weapon = guns.get(PlayerAction.SECONDWEAPON);
    }

    if ((act = getFromInput(PlayerAction.SHOOT, context)).get() && weapon != null) {
      weapon.action(context, this);
      act.set(false);
    }
    if ((act = getFromInput(PlayerAction.RELOAD, context)).get() && weapon != null) {
      weapon.updateUse();
      act.set(false);
    }
    //    todo maybe add some other func
  }

  private AtomicBoolean getFromInput(final PlayerAction action, final GameEngine context) {
    return context.getInput().get(action);
  }

  @Override
  public void checkCollisions(final GameEngine context) {
//  todo
  }
}