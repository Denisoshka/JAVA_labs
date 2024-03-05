package ru.nsu.zhdanov.lab_3.model.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.BaseBullet;

import static ru.nsu.zhdanov.lab_3.model.entity.Constants.ItIsGoingToHurtBulletC.LIVES_QUANTITY;
import static ru.nsu.zhdanov.lab_3.model.entity.Constants.ItIsGoingToHurtBulletC.SHIFT;
import static ru.nsu.zhdanov.lab_3.model.entity.Constants.ItIsGoingToHurtBulletC.DAMAGE;

public class ItIsGoingToHurtBullet extends BaseBullet {

  public ItIsGoingToHurtBullet(Fraction fraction, int x, int y, double cos, double sin) {
    super(ContextID.ItIsGoingToHurtBullet, fraction, x, y, cos, sin, SHIFT, DAMAGE, LIVES_QUANTITY, 0);
  }
}