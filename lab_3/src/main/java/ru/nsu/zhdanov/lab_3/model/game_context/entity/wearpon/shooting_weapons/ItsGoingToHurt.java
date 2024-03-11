package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Constants;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;

public class ItsGoingToHurt extends ShootingWeapon implements Constants.ItsGoingToHurtC {


  public ItsGoingToHurt() {
    super(ContextID.ItIsGoingToHurt, DAMAGE, CAPACITY);
  }

  @Override
  public void action(GameEngine context, Entity user) {
    context.getActionTraceBuffer().add(new ItIsGoingToHurtBullet(user.getFraction(), user.getX(), user.getY(), user.getCosDir(), user.getSinDir()));
  }

  @Override
  public boolean readyForUse() {
    return occupancy > 0;
  }
}
