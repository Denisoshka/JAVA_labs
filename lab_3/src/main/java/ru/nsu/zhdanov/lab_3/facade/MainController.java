package ru.nsu.zhdanov.lab_3.facade;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.facade.exceptions.ResourceNotAvailable;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel.Score;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class MainController implements MainControllerRequests.GameContext, MainControllerRequests.MenuContext {
  final AtomicBoolean scoreUpdating;

  private MenuController menuController = null;
  private GameController gameController = null;
  final private @Getter Stage primaryStage;

  private final MainModel model;

  public MainController(Properties menuProperties, Properties gameProperties, Stage primaryStage) {
    this.scoreUpdating = new AtomicBoolean(false);
//    this.gameController = new GameController(
    this.primaryStage = primaryStage;
    Properties properties = new Properties();
    try{
      properties.load(getClass().getResourceAsStream("/model/main_model.properties"));
    }catch (IOException e){
      throw new RuntimeException(e);
    }

    this.model = new MainModel(properties);
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
      controller.setContext(properties, this, primaryStage);
      primaryStage.show();
      return controller;
    } catch (NullPointerException | IOException e) {
      throw new ResourceNotAvailable(e);
    }
  }

  public void setGameScreen() {
    Properties properties = new Properties();
    try {
      properties.load(getClass().getResourceAsStream("/facade/properties/game_controller.properties"));
    } catch (IOException e) {
//      todo
      throw new RuntimeException(e);
    }
    gameController = (GameController) changeScene(properties, "/facade/screens/game_window.fxml");
    gameController.perform();
  }

  public void setMenuScreen() {
    Properties properties = new Properties();
    try {
      properties.load(getClass().getResourceAsStream("/facade/properties/menu_controller.properties"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    changeScene(properties, "/facade/screens/menu_window.fxml");
  }

  public List<String> acquireScore() {
    return model.acquireScore();
  }

  @Override
  public void startGame(String name) {
    model.setPlayerName(name);
    setGameScreen();
  }

  @Override
  public String getPlayerName() {
    return model.getPlayerName();
  }

  @Override
  public void gameEnd() {
    setMenuScreen();
  }

  @Override
  public void dumpScore(String name, int score) {
    model.dumpScore(new Score(name, score));
  }
}


