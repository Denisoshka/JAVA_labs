package ru.nsu.zhdanov.lab_3.lab_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.zhdanov.lab_3.fassade.MainController;

import java.io.IOException;

public class GameApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(GameApplication.class.getResource("game_window.fxml"));
    Scene scene = new Scene(loader.load());

    stage.setTitle("Full Screen Game Example");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}