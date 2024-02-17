package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.ShootingWeapon;

public class ItsGoingToHurt extends ShootingWeapon {
  private static final int BASEDAMAGE = 10;
  private static final int BASEOCCUPANCY = 10;

  public ItsGoingToHurt() {
    super(ContextID.ItIsGoingToHurt, BASEDAMAGE, BASEOCCUPANCY);
  }

  @Override
  public boolean action(GameEngine context, Entity user) {
    context.getEntities().add(new BaseBullet(user.getX(), user.getY(), user.getCosDir(), user.getSinDir()));
    return false;
  }
}
