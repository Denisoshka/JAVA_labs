package ru.nsu.zhdanov.lab_3;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.nsu.zhdanov.lab_3.fx_facade.MainController;

import java.io.IOException;

public class FXGame extends Application {
  private Stage primaryStage;
  @Override
  public void start(Stage primaryStage) throws IOException {
    this.primaryStage = primaryStage;
    MainController mainController = new MainController(null, null, this.primaryStage);
    mainController.perform();
  }

  public static void main(String[] args) {
    launch();
  }
}