package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Constants.RocketLauncherC;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;

public class RocketLauncher extends ShootingWeapon implements RocketLauncherC {
  private long lastShot;

  public RocketLauncher() {
    super(ContextID.RocketLauncher, 0, CAPACITY);
  }

  @Override
  public void action(GameEngine context, Entity user) {
    if (System.currentTimeMillis() - lastShot > DELAY) {
      occupancy = CAPACITY;
    }
    if (occupancy > 0) {
      context.getActionTraceBuffer().add(new RocketLauncherBullet(user.getFraction(), user.getX(), user.getY(),
              user.getCosDir(), user.getSinDir()));
      lastShot = System.currentTimeMillis();
      --occupancy;
    }
  }

  @Override
  public boolean readyForUse() {
    return occupancy > 0;
  }
}
