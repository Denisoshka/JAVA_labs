package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Constants.RocketLauncherC;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile.RocketLauncherBullet;

public class RocketLauncher extends ShootingWeapon implements RocketLauncherC {
  private long lastShot;

  public RocketLauncher() {
    super(ContextID.RocketLauncher, 0, CAPACITY);
  }

  @Override
  public void update(GameContext context, PlayerController user) {
    if (System.currentTimeMillis() - lastShot > DELAY) {
      occupancy = CAPACITY;
    }
  }

  @Override
  public void action(GameContext context, PlayerController user) {
    if (occupancy <= 0) {
      return;
    }
    context.submitAction(
            new RocketLauncherBullet(
                    user.getFraction(), user.getX(), user.getY(),
                    user.getCosDir(), user.getSinDir()
            )
    );
    lastShot = System.currentTimeMillis();
    --occupancy;
  }
}
