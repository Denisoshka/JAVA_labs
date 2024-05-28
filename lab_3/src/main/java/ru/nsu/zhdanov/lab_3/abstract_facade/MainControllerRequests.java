package ru.nsu.zhdanov.lab_3.abstract_facade;

import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;

public interface MainControllerRequests {
  interface MenuContext {
    void performGameScreen(String name);

    List<MainModel.Score> acquireScore();
  }

  interface GameContext {
    String getPlayerName();

    void shutdownGameScreen();

    void dumpScore(String name, int score);
  }
}
