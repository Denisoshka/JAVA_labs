package ru.nsu.zhdanov.lab_3.abstract_facade;

import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;

public interface MainControllerRequests {
  interface MenuContext {
    void performGame(String name);

    List<MainModel.Score> acquireScore();
  }

  interface GameContext {
    String getPlayerName();

    void shutdownGame();

    void dumpScore(String name, int score);
  }
}
