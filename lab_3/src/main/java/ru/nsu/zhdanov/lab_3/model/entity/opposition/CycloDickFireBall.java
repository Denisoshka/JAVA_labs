package ru.nsu.zhdanov.lab_3.model.entity.opposition;

import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.BaseBullet;

import static ru.nsu.zhdanov.lab_3.model.entity.Constants.CycloDickFireBallC;

public class CycloDickFireBall extends BaseBullet {

  public CycloDickFireBall(int x, int y, double cos, double sin) {
    super(ContextID.CycloDickFireBall,
            Fraction.OPPOSITION,
            x, y, cos, sin,
            CycloDickFireBallC.SHIFT,
            CycloDickFireBallC.DAMAGE,
            CycloDickFireBallC.LIVES,
            CycloDickFireBallC.RADIUS);
  }
}
