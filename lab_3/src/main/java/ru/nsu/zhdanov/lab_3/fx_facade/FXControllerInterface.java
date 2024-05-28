package ru.nsu.zhdanov.lab_3.fx_facade;

import javafx.stage.Stage;

import java.util.Properties;

public interface FXControllerInterface {
  abstract void setContext(Properties properties, MainController controller, Stage stage);
}
