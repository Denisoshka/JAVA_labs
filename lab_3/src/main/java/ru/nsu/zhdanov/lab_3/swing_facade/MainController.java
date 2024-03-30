package ru.nsu.zhdanov.lab_3.swing_facade;

import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;
import java.util.Properties;

public class MainController implements MainControllerRequests.GameContext, MainControllerRequests.MenuContext {
  MainControllerRequests.MenuContext menuReq;

  public void changeScene(Properties properties){}


  @Override
  public void startGame(String name) {
  }

  @Override
  public List<MainModel.Score> acquireScore() {
    return null;
  }

  @Override
  public String getPlayerName() {
    return null;
  }

  @Override
  public void gameEnd() {
  }

  @Override
  public void dumpScore(String name, int score) {
  }
}
