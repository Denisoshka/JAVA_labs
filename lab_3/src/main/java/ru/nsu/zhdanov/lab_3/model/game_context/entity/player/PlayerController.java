package ru.nsu.zhdanov.lab_3.model.game_context.entity.player;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Constants.PlayerC;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.Weapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons.ItsGoingToHurt;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons.RocketLauncher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class PlayerController extends Entity implements PlayerC {
  final private Map<PlayerAction, ShootingWeapon> guns;
  private @Getter ShootingWeapon weapon;

  public PlayerController(int x, int y) {
    super(x, y, PlayerC.RADIUS, 0, 0, 0, LIVES_QUANTITY, ContextType.EntityT, ContextID.Player, Fraction.PLAYER);
    this.guns = new HashMap<>();
    this.guns.put(PlayerAction.FIRSTWEAPON, new ItsGoingToHurt());
    this.guns.put(PlayerAction.SECONDWEAPON, new RocketLauncher());
    this.weapon = guns.get(PlayerAction.FIRSTWEAPON);
  }

  @Override
  public void update(final GameContext context) {
    int dx = context.getCursorXPos() - x;
    int dy = context.getCursorYPos() - y;
    double diag = Math.hypot(dx, dy);
    sinDir = (double) dy / diag;
    cosDir = (double) dx / diag;

    handleMove(context);
    handleAction(context);
//    todo finish implementation
  }

  protected void handleMove(final GameContext context) {
    this.xShift = 0;
    this.yShift = 0;
    if (getFromInput(PlayerAction.FORWARD, context).get()) {
      this.yShift -= MOVE_COEF;// NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.LEFT, context).get()) {
      this.xShift -= MOVE_COEF;// NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.RIGHT, context).get()) {
      this.xShift += MOVE_COEF;//NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.BACK, context).get()) {
      this.yShift += MOVE_COEF;// NONFORWARDMOVECOEF;
    }
    if (getFromInput(PlayerAction.SPEEDUP, context).get()) {
      this.xShift = (int) (this.xShift * SPEEDUP_COEF);
    }
    this.yShift = context.getMap().getAllowedYShift(this);
    this.xShift = context.getMap().getAllowedXShift(this);

    x += xShift;
    y += yShift;
  }

  private void handleAction(GameContext context) {
    if (getFromInput(PlayerAction.FIRSTWEAPON, context).get()) {
      weapon = guns.get(PlayerAction.FIRSTWEAPON);
    }
    if (getFromInput(PlayerAction.SECONDWEAPON, context).get()) {
      weapon = guns.get(PlayerAction.SECONDWEAPON);
    }
    if (weapon != null) {
      weapon.update(context, this);
    }

    AtomicBoolean act;
    if ((act = getFromInput(PlayerAction.SHOOT, context)).get() && weapon != null) {
      weapon.action(context, this);
      act.set(false);
    }
/*
    if ((act = getFromInput(PlayerAction.RELOAD, context)).get() && weapon != null) {
      act.set(false);
    } todo
 */
    //    todo maybe add some other func
  }

  private AtomicBoolean getFromInput(final PlayerAction action, final GameContext context) {
    return context.getInput().get(action);
  }

  @Override
  public void checkCollisions(final GameContext context) {
  }
}