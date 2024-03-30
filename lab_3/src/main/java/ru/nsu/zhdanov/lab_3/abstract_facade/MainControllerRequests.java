package ru.nsu.zhdanov.lab_3.abstract_facade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;

public interface MainControllerRequests {
  interface MenuContext {
    void startGame(String name);

    List<MainModel.Score> acquireScore();
  }

  interface GameContext {
    String getPlayerName();

    void gameEnd();

    void dumpScore(String name, int score);
  }
}
