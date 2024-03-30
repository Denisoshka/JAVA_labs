package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.BaseBullet;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;

public class CycloDickFireBall extends BaseBullet {
  private static final int LIVES = 1;
  private static final int RADIUS = 15;
  private static final int SHIFT = 6;
  private static final int DAMAGE = 100;

  public CycloDickFireBall(int x, int y, double cos, double sin) {
    super(ContextID.CycloDickFireBall, Fraction.OPPOSITION,
            x, y, cos, sin, SHIFT, DAMAGE, LIVES, RADIUS);
  }
}
