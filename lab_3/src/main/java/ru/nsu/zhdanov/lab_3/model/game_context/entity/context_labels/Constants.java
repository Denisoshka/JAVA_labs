package ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels;


public interface Constants {
  interface gameEngineConstants {
  }

  interface PlayerC {
    int LIVES_QUANTITY = 300;
    int RADIUS = 25;
    int MOVE_COEF = 4;
    double SPEEDUP_COEF = 1.5;
  }

  interface CycloDickC {
    int REWARD = 10;
    int LIVES = 300;
    int RADIUS = 30;
    int DEF_SHIFT = 1;
    int TRACK_SHIFT = 3;
    double ATTACK_DIST = 250;
    double TRACK_DEGREE_DEVIATION = 10;
    double DEF_DEGREE_DEVIATION = 40;
  }

  interface CycloDickFireBallC {
    int LIVES = 1;
    int RADIUS = 15;
    int SHIFT = 6;
    int DAMAGE = 10;
  }

  interface AxeC {
    int ACTION_DISTANCE = 60;
    int AXE_DAMAGE = 120;
  }

  interface ItIsGoingToHurtBulletC {
    int DAMAGE = 50;
    int LIVES_QUANTITY = 1;
    int SHIFT = 10;
  }

  interface ItsGoingToHurtC {
    int DAMAGE = 25;
    int CAPACITY = 35;
    int DELAY = 1250;
  }

  interface RocketLauncherC {
    int CAPACITY = 7;
    int DELAY = 2500;
  }

  interface RocketLauncherBulletC {
    int SHIFT = 6;
    int DAMAGE = 170;
    int RADIUS = 5;
    int LIVES_QUANTITY = 1;
    int DAMAGE_RADIUS = 50;
    int TRACKING_RADIUS = 100;
  }

  interface TwoBarrelsC {
    int LIVES = 250;
    int RADIUS = 20;
    int REWARD = 30;
    int SHOOTS_QUANTITY = 4;
    int DEF_SHIFT = 3;
    int DELAY = 1500;
    double ATTACK_DIST = 250;
    double BULLET_DEGREE_DEVIATION = 5;
    double BULLET_DEGREE_DEVIATION_COEF = BULLET_DEGREE_DEVIATION * 2;
  }
}
