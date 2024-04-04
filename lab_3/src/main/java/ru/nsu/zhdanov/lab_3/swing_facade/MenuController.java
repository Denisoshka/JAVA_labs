package ru.nsu.zhdanov.lab_3.swing_facade;

import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.abstract_facade.SubControllerRequests;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.List;

public class MenuController implements SubControllerRequests {
  private final MainControllerRequests.MenuContext menuReq;

  public void startGame() {
    menuReq.performGameScreen(null);
  }

  public void exitGame() {
    System.exit(0);
  }

  public MenuController(MainController controller) {
    menuReq = controller;
  }

  public String[][] acquireScore() {
    List<MainModel.Score> scores = menuReq.acquireScore();
    String[][] dataArray = new String[scores.size()][2];
    for (int i = 0; i < scores.size(); ++i) {
      dataArray[i][0] = scores.get(i).getName();
      dataArray[i][1] = String.valueOf(scores.get(i).getScore());
    }
    return dataArray;
  }

  @Override
  public void perform() {
  }

  @Override
  public void shutdown() {
  }
}