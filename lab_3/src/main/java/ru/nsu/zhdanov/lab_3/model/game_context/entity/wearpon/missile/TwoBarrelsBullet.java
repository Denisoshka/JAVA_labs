package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Constants;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.BaseBullet;

public class TwoBarrelsBullet extends BaseBullet implements Constants.TwoBarrelsBulletC {
  public TwoBarrelsBullet(int x, int y, double cos, double sin) {
    super(ContextID.TwoBarrelsBullet, Fraction.OPPOSITION, x, y, cos, sin,
            SHIFT, DAMAGE, LIVES_QUANTITY, RADIUS);
  }

  public TwoBarrelsBullet(Fraction fraction, int x, int y, double cos, double sin) {
    super(ContextID.TwoBarrelsBullet, fraction, x, y, cos, sin,
            SHIFT, DAMAGE, LIVES_QUANTITY, RADIUS);
  }
}
