package ru.nsu.zhdanov.lab_3.model.entity.wearpon.shooting_weapons;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.ShootingWeapon;

import static ru.nsu.zhdanov.lab_3.model.entity.Constants.ItsGoingToHurtC.DAMAGE;
import static ru.nsu.zhdanov.lab_3.model.entity.Constants.ItsGoingToHurtC.CAPACITY;


@Slf4j
public class ItsGoingToHurt extends ShootingWeapon {


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
