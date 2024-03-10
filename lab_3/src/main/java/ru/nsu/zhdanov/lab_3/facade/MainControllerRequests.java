package ru.nsu.zhdanov.lab_3.facade;

import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;

public interface MainControllerRequests {
  interface MenuContext {
    public abstract void startGame(String name);

    public abstract List<String> acquireScore();
  }

  interface GameContext {
    public abstract String getPlayerName();

    public abstract void gameEnd();

    public abstract void dumpScore(String name, int score);
  }
}
