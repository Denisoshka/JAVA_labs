package ru.nsu.zhdanov.lab_3.fx_facade;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.abstract_facade.SubControllerRequests;
import ru.nsu.zhdanov.lab_3.fx_facade.exceptions.ResourceNotAvailable;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class MainController implements MainControllerRequests.GameContext, MainControllerRequests.MenuContext {
  private MenuController menuController = null;
  private GameController gameController = null;


  final private Stage primaryStage;

  private final MainModel model;

  public MainController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.model = new MainModel();
  }

  public void perform() throws IOException {
    performMenuScreen();
  }

  public SubControllerRequests changeScene(Properties properties, String FXMLPath) {
    Scene scene;
    FXMLLoader loader;
    try {
      loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(FXMLPath)));
      scene = new Scene(Objects.requireNonNull(loader.load()));
    } catch (IOException e) {
      throw new ResourceNotAvailable(FXMLPath, e);
    }
    primaryStage.setScene(scene);
    SubControllerRequests controller = loader.getController();
    ((FXControllerInterface) controller).setContext(properties, this, primaryStage);
    primaryStage.show();
    return controller;
  }

  public void performGameScreen() {
    Properties properties = new Properties();
    String gameControllerProp = "properties/game_controller.properties";
    try (var reader = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(gameControllerProp)))) {
      properties.load(reader);
    } catch (IOException | NullPointerException | IllegalArgumentException e) {
      throw new ResourceNotAvailable(gameControllerProp, e);
    }
    gameController = (GameController) changeScene(properties, "screens/game_window.fxml");
    gameController.perform();
  }

  public void performMenuScreen() {
    Properties properties = new Properties();
    String menuControllerProp = "properties/menu_controller.properties";
    try (var reader = new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(menuControllerProp)))) {
      properties.load(reader);
    } catch (IOException | NullPointerException | IllegalArgumentException e) {
      throw new ResourceNotAvailable(menuControllerProp, e);
    }
    menuController = (MenuController) changeScene(properties, "screens/menu_window.fxml");
    menuController.perform();
  }

  public List<MainModel.Score> acquireScore() {
    return model.acquireScore();
  }

  @Override
  public void performGameScreen(String name) {
    menuController.shutdown();
    menuController = null;
    model.setPlayerName(name);
    performGameScreen();
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  @Override
  public String getPlayerName() {
    return model.getPlayerName();
  }

  @Override
  public void shutdownGameScreen() {
    gameController.shutdown();
    gameController = null;
    performMenuScreen();
  }

  @Override
  public void dumpScore(String name, int score) {
    model.dumpScore(new MainModel.Score(name, score));
  }
}


