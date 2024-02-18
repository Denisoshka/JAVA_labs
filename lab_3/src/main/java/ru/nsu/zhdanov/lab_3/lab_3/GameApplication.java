package ru.nsu.zhdanov.lab_3.lab_3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;

import java.io.IOException;

public class GameApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    java.net.URL res = GameApplication.class.getClassLoader().getResource("game_window.fxml");
    FXMLLoader fxmlLoader = new FXMLLoader(res);

    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Car factory!");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}