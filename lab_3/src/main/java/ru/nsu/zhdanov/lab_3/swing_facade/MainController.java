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
  public void performGameScreen(String name) {
    menuView.shutdown();
    menuView = null;
    mainModel.setPlayerName(name);
    Properties properties = new Properties();
    String menuControllerProp = "properties/game_controller.properties";
    try {
      properties.load(Objects.requireNonNull(getClass().getResourceAsStream(menuControllerProp)));
    } catch (IOException | NullPointerException e) {
      throw new RuntimeException("unable to load " + menuControllerProp);
    }
    gameView = new GameView(properties, this);
    gameView.perform();
  }

  @Override
  public void shutdownGameScreen() {
    gameView.shutdown();
    gameView = null;
    performMenu();
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
