package ru.nsu.zhdanov.lab_3.facade;

import javafx.stage.Stage;

import java.util.Properties;

public interface SubControllerRequests {
  abstract void setContext(Properties properties, MainController controller, Stage primaryStage);

  abstract void perform();

  abstract void shutdown();
}
