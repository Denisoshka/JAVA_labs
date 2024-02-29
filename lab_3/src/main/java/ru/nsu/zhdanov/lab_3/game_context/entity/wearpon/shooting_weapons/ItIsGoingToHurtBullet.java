package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.BaseBullet;

public class ItIsGoingToHurtBullet extends BaseBullet {
  public final static int damage = 10;
  public final static int livesQuantity = 1;
  public final static int spriteRadius = 6;
  public final static int shift = 4;

  public ItIsGoingToHurtBullet(int x, int y, double cos, double sin) {
    super(ContextID.ItIsGoingToHurtBullet, x, y, cos, sin, shift, damage, livesQuantity, 0, spriteRadius);
  }
}
