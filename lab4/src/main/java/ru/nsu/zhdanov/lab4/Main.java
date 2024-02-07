package ru.nsu.zhdanov.lab4;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    Application.launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Hello world Application");
    stage.show();
  }
}