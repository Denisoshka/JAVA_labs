package ru.nsu.zhdanov.lab_3.model.entity.opposition;

import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.BaseBullet;

public class CycloDickFireBall extends BaseBullet {
  public final static int radius = 20;
  public final static int shift = 3;
  public final static int damage = 10;

  public CycloDickFireBall(int x, int y, double cos, double sin) {
    super(ContextID.CycloDickFireBall, x, y, cos, sin, shift, damage, 1, radius);
  }
}
