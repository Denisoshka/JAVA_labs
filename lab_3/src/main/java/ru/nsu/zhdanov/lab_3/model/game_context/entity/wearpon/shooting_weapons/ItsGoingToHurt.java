package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile.ItIsGoingToHurtBullet;

public class ItsGoingToHurt extends ShootingWeapon {
  private static final int DAMAGE = 250;
  private static final int CAPACITY = 35;
  private static final int DELAY = 1250;


  private long lastShot;

  public ItsGoingToHurt() {
    super(ContextID.ItIsGoingToHurt, DAMAGE, CAPACITY);
  }

  @Override
  public void update(GameSession context, PlayerController user) {
    if (System.currentTimeMillis() - lastShot <= DELAY) {
      return;
    }
    occupancy = CAPACITY;
  }

  @Override
  public void action(GameSession context, PlayerController user) {
    if (occupancy <= 0) {
      return;
    }
    lastShot = System.currentTimeMillis();
    context.submitAction(new ItIsGoingToHurtBullet(user.getFraction(), user.getX(), user.getY(), user.getCosDir(), user.getSinDir()));
    --occupancy;
  }
}
