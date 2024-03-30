package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.BaseBullet;

public class ItIsGoingToHurtBullet extends BaseBullet {
  private static final int DAMAGE = 500;
  private static final int LIVES_QUANTITY = 1;
  private static final int SHIFT = 10;

  public ItIsGoingToHurtBullet(Fraction fraction, int x, int y, double cos, double sin) {
    super(ContextID.ItIsGoingToHurtBullet, fraction, x, y, cos, sin, SHIFT, DAMAGE, LIVES_QUANTITY, 0);
  }
}
