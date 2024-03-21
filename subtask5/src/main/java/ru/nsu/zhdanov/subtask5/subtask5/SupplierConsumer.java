package ru.nsu.zhdanov.subtask5.subtask5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.zhdanov.subtask5.subtask5.view.SupplierConsumerController;

import java.io.IOException;

public class SupplierConsumer extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(SupplierConsumer.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    ((SupplierConsumerController) fxmlLoader.getController()).setDependencies(stage);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}