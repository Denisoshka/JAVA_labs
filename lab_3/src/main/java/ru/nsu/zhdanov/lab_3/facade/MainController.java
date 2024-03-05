package ru.nsu.zhdanov.lab_3.facade;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.facade.exceptions.ResourceNotAvailable;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class MainController implements MenuRequests {
  final AtomicBoolean scoreUpdating;

  private MenuController menuController = null;
  private GameController gameController = null;
  final private Stage primaryStage;

  public MainController(Properties menuProperties, Properties gameProperties, Stage primaryStage) {
    this.scoreUpdating = new AtomicBoolean(false);
//    this.gameController = new GameController(
    this.primaryStage = primaryStage;
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
      controller.setContext(properties, this);
//      primaryStage.initStyle(StageStyle.UNDECORATED);
      primaryStage.show();
      return controller;
    } catch (NullPointerException | IOException e) {
      throw new ResourceNotAvailable(e);
    }
  }

  public void setGameScreen() {
    Properties properties;
    try {
      properties = new Properties();
      properties.load(getClass().getResourceAsStream("/facade/properties/game_controller.properties"));
    } catch (IOException e) {
//      todo
      throw new RuntimeException(e);
    }
    gameController = (GameController) changeScene(properties, "/facade/screens/game_window.fxml");
    primaryStage.setFullScreen(true);
    gameController.perform();
  }

  public void setMenuScreen() {
    Properties properties;
    try (var res = getClass().getResourceAsStream("/facade/properties/menu_controller.properties")) {
      properties = new Properties();
      properties.load(res);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    changeScene(properties, "/facade/screens/menu_window.fxml");
  }

  @Override
  public void menuStartGame() {
    setGameScreen();
  }
}


