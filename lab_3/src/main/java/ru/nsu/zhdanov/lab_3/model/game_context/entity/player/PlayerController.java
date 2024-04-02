package ru.nsu.zhdanov.lab_3.model.game_context.entity.player;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons.ItsGoingToHurt;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons.RocketLauncher;
import ru.nsu.zhdanov.lab_3.model.game_context.interfaces.WeaponImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class PlayerController extends Entity {
  final static int LIVES_QUANTITY = 3;
  final static int RADIUS = 25;
  final static int MOVE_COEF = 4;
  final static int HILL = 1;
  final static int TO_HILL_DELAY = 7500;
  final static double SPEEDUP_COEF = 1.5;

  final private Map<PlayerAction, ShootingWeapon> guns;
  private long lastDamage;
  private @Getter ShootingWeapon weapon;

  public PlayerController(int x, int y) {
    super(x, y, RADIUS, 0, 0, 0, LIVES_QUANTITY, ContextType.EntityT, ContextID.Player, Fraction.PLAYER);
    this.guns = new HashMap<>();
    this.guns.put(PlayerAction.FIRSTWEAPON, new ItsGoingToHurt());
    this.guns.put(PlayerAction.SECONDWEAPON, new RocketLauncher());
    this.weapon = guns.get(PlayerAction.FIRSTWEAPON);
  }

  @Override
  public void update(final GameSession context) {
    int dx = context.getCursorXPos() - x;
    int dy = context.getCursorYPos() - y;
    double diag = Math.hypot(dx, dy);
    sinDir = (double) dy / diag;
    cosDir = (double) dx / diag;
    if (System.currentTimeMillis() - lastDamage >= TO_HILL_DELAY) {
      if (livesQuantity >= LIVES_QUANTITY) {
        livesQuantity = LIVES_QUANTITY;
      } else {
        livesQuantity += HILL;
      }
    }
    handleMove(context);
    handleAction(context);
  }

  protected void handleMove(final GameSession context) {
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

  private void handleAction(GameSession context) {
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
  }

  private AtomicBoolean getFromInput(final PlayerAction action, final GameSession context) {
    return context.getInputTrace().get(action);
  }

  @Override
  public void acceptDamage(WeaponImpl ent) {
    lastDamage = System.currentTimeMillis();
    super.acceptDamage(ent);
  }

  @Override
  public void checkCollisions(final GameSession context) {
  }
}