package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.ShootingWeapon;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;


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

  @Override
  public void drawWeaponSprite(DrawInterface drawContext, int x, int y, double cosDir, double sinDir, boolean reflect) {
    drawContext.draw(ID, x, y, 30, 20, cosDir, sinDir, false);
  }
}
