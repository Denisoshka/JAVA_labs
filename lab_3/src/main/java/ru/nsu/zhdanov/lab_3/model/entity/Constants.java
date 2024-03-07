package ru.nsu.zhdanov.lab_3.model.entity;


public interface Constants {
  interface gameEngineConstants{
  }

  interface PlayerC {
    int LIVES_QUANTITY = 200;
    int RADIUS = 25;
    int MOVE_COEF = 2;
  }

  interface CycloDickC {
    int REWARD = 10;
    int LIVES = 200;
    int RADIUS = 30;
    int DEF_SHIFT = 1;
    int TRACK_SHIFT = 3;
    int WANT_TO_MOVE_QUANTITY = 10;
    double ATTACK_DIST = 200;
    double TRACK_DEGREE_DEVIATION = 10;
    double TRACK_DEGREE_DEVIATION_COEF = TRACK_DEGREE_DEVIATION * 2;
    double DEF_DEGREE_DEVIATION = 30;
    double DEF_DEGREE_DEVIATION_COEF = TRACK_DEGREE_DEVIATION * 2;
  }

  interface CycloDickFireBallC {
    int LIVES = 1;
    int RADIUS = 15;
    int SHIFT = 3;
    int DAMAGE = 10;
  }

  interface AxeC {
    int ACTION_DISTANCE = 40;
    int AXE_DAMAGE = 20;
  }

  interface ItIsGoingToHurtBulletC {
    int DAMAGE = 10;
    int LIVES_QUANTITY = 1;
    int SHIFT = 4;
  }

  interface ItsGoingToHurtC {
    int DAMAGE = 10;
    int CAPACITY = 10;
  }

  interface EngC{
    int CycloDickReward = 10;
  }
}
