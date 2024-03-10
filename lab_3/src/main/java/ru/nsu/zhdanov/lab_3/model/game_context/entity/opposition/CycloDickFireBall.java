package ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.Fraction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.BaseBullet;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;

import static ru.nsu.zhdanov.lab_3.model.game_context.entity.Constants.CycloDickFireBallC;

public class CycloDickFireBall extends BaseBullet implements CycloDickFireBallC{

  public CycloDickFireBall(int x, int y, double cos, double sin) {
    super(ContextID.CycloDickFireBall,
            Fraction.OPPOSITION,
            x, y, cos, sin,
            SHIFT,
            DAMAGE,
            LIVES,
            RADIUS);
  }
}
