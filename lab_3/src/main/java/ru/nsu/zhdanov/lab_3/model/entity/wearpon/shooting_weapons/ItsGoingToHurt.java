package ru.nsu.zhdanov.lab_3.model.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.Constants;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.ShootingWeapon;

public class ItsGoingToHurt extends ShootingWeapon implements Constants.ItsGoingToHurtC {


  public ItsGoingToHurt() {
    super(ContextID.ItIsGoingToHurt, DAMAGE, CAPACITY);
  }

  @Override
  public void action(GameEngine context, Entity user) {
    if (occupancy > 0) {
      context.getActionTraceBuffer().add(new ItIsGoingToHurtBullet(user.getFraction(), user.getX(), user.getY(), user.getCosDir(), user.getSinDir()));
      --occupancy;
    }
  }

  @Override
  public boolean readyForUse() {
    return occupancy > 0;
  }
}
