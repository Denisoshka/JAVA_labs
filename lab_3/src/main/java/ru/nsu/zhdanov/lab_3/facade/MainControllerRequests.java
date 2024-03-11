package ru.nsu.zhdanov.lab_3.facade;

import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;

public interface MainControllerRequests {
  interface MenuContext {
    void startGame(String name);

    List<String> acquireScore();
  }

  interface GameContext {
    String getPlayerName();

    void gameEnd();

    void dumpScore(String name, int score);
  }
}
