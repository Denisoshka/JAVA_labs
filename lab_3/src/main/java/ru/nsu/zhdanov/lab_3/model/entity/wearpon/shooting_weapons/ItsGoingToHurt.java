package ru.nsu.zhdanov.lab_3.model.entity.wearpon.shooting_weapons;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.ShootingWeapon;


@Slf4j
public class ItsGoingToHurt extends ShootingWeapon {
  private static final int BASEDAMAGE = 10;
  private static final int BASEOCCUPANCY = 10;

  public ItsGoingToHurt() {
    super(ContextID.ItIsGoingToHurt, BASEDAMAGE, BASEOCCUPANCY);
  }

  @Override
  public void action(GameEngine context, Entity user) {
    context.getActionTraceBuffer().add(new ItIsGoingToHurtBullet(user.getX(), user.getY(), user.getCosDir(), user.getSinDir()));
  }

}
