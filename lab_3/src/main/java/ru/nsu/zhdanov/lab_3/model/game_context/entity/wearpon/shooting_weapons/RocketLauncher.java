package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.shooting_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.missile.RocketLauncherBullet;

public class RocketLauncher extends ShootingWeapon{
  private static final int CAPACITY = 7;
  private static final int DELAY = 2500;

  private long lastShot;

  public RocketLauncher() {
    super(ContextID.RocketLauncher, 0, CAPACITY);
  }

  @Override
  public void update(GameSession context, PlayerController user) {
    if (System.currentTimeMillis() - lastShot > DELAY) {
      occupancy = CAPACITY;
    }
  }

  @Override
  public void action(GameSession context, PlayerController user) {
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
