package ru.nsu.zhdanov.lab_3.game_context.entity.opposition;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.base_weapons.BaseBullet;

public class CycloDickFireBall extends BaseBullet {
  public final static int radius = 10;
  public final static int spriteRadius = radius;
  public final static int shift = 3;
  public final static int damage = 30;

  public CycloDickFireBall(int x, int y, double cos, double sin) {
    super(ContextID.CycloDickFireBall, x, y, cos, sin, shift, damage, 1, radius, spriteRadius);
  }
}
