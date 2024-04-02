package ru.nsu.zhdanov.lab_3.swing_facade;

import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MainController implements MainControllerRequests.GameContext, MainControllerRequests.MenuContext {
  MainControllerRequests.MenuContext menuReq;
  private MenuView menuView;
  private GameView gameView;
  private MainModel mainModel;

  public MainController() {
    this.mainModel = new MainModel();
  }

  public void performMenu() {
    this.menuView = new MenuView(this);
    this.menuView.perform();
  }

  @Override
  public void performGame(String name) {
    this.menuView.shutdown();
    this.menuView = null;
    this.mainModel.setPlayerName(name);
    Properties properties = new Properties();
    String menuControllerProp = "properties/game_controller.properties";
    try {
      properties.load(Objects.requireNonNull(getClass().getResourceAsStream(menuControllerProp)));
    } catch (IOException | NullPointerException e) {
      throw new RuntimeException("unable to load " + menuControllerProp);
    }
    this.gameView = new GameView(properties, this);
    this.gameView.perform();
  }

  @Override
  public void shutdownGame() {
    this.gameView.shutdown();
    this.gameView = null;
    this.performMenu();
  }

  @Override
  public List<MainModel.Score> acquireScore() {
    return mainModel.acquireScore();
  }

  @Override
  public String getPlayerName() {
    return null;
  }


  @Override
  public void dumpScore(String name, int score) {
  }
}
