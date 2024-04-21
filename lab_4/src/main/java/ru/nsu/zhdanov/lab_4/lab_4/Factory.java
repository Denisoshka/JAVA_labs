package ru.nsu.zhdanov.lab_4.lab_4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.zhdanov.lab_4.facade.MainController;

import java.io.IOException;
import java.util.Objects;

public class Factory extends javafx.application.Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("FactoryView.fxml")));
    Scene scene = new Scene(fxmlLoader.load());
    ((MainController) fxmlLoader.getController()).setPrimaryStage(stage);
    stage.setTitle("da ne krady ia labi");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}