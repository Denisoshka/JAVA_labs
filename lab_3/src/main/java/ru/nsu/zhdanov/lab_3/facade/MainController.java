package ru.nsu.zhdanov.lab_3.facade;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.facade.exceptions.FacadeException;
import ru.nsu.zhdanov.lab_3.facade.exceptions.LoaderNotAvailable;
import ru.nsu.zhdanov.lab_3.facade.exceptions.ResourceNotAvailable;

import java.io.IOException;
import java.net.URL;
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

  public void changeScene(Object controller, String path) {
    try {
      FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
//      loader.setController(controller);
      primaryStage.setScene(new Scene(Objects.requireNonNull(loader.load())));
      gameController = loader.getController();
      gameController.setContext(null);
      primaryStage.show();
    } catch (NullPointerException | IOException e) {
      throw new ResourceNotAvailable(e);
    }
  }

  public void setGameScreen() {
    gameController.setContext(null);
    changeScene(gameController, "/facade/game_window.fxml");
    gameController.perform();
//    todo maybe in othre thread make
  }

  public void setMenuScreen() throws IOException {
    changeScene(menuController, "/facade/menu_window.fxml");
  }

  @Override
  public void menuStartGame() {
    setGameScreen();
  }
}


