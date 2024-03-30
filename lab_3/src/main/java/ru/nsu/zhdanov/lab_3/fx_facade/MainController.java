package ru.nsu.zhdanov.lab_3.fx_facade;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.abstract_facade.SubControllerRequests;
import ru.nsu.zhdanov.lab_3.fx_facade.exceptions.ResourceNotAvailable;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


public class MainController implements MainControllerRequests.GameContext, MainControllerRequests.MenuContext {
  private MenuController menuController = null;
  private GameController gameController = null;
  final private @Getter Stage primaryStage;

  private final MainModel model;

  public MainController(Properties menuProperties, Properties gameProperties, Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.model = new MainModel();
  }

  public void perform() throws IOException {
    setMenuScreen();
  }

  public SubControllerRequests changeScene(Properties properties, String FXMLPath) {
    try {
      FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXMLPath)));
      Scene scene = new Scene(Objects.requireNonNull(loader.load()));
      primaryStage.setScene(scene);
      SubControllerRequests controller = loader.getController();
      ((FXControllerInterface) controller).setContext(properties, this, primaryStage);
      primaryStage.show();
      return controller;
    } catch (NullPointerException | IOException e) {
      throw new ResourceNotAvailable(e);
    }
  }

  public void setGameScreen() {
    Properties properties = new Properties();
    try {
      properties.load(getClass().getResourceAsStream("properties/game_controller.properties"));
    } catch (IOException e) {
//      todo
      throw new RuntimeException(e);
    }
    gameController = (GameController) changeScene(properties, "screens/game_window.fxml");
    gameController.perform();
  }

  public void setMenuScreen() {
    Properties properties = new Properties();
    try {
      properties.load(getClass().getResourceAsStream("properties/menu_controller.properties"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    menuController = (MenuController) changeScene(properties, "screens/menu_window.fxml");
    menuController.perform();
  }

  public List<MainModel.Score> acquireScore() {
    return model.acquireScore();
  }

  @Override
  public void startGame(String name) {
    menuController.shutdown();
    model.setPlayerName(name);
    setGameScreen();
  }

  @Override
  public String getPlayerName() {
    return model.getPlayerName();
  }

  @Override
  public void gameEnd() {
    gameController.shutdown();
    setMenuScreen();
  }

  @Override
  public void dumpScore(String name, int score) {
    model.dumpScore(new MainModel.Score(name, score));
  }
}


