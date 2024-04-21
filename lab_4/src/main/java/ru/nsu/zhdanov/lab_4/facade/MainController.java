package ru.nsu.zhdanov.lab_4.facade;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MainController {
  @FXML
  private SectionStateView engineState;
  @FXML
  private SectionStateView bodyState;
  @FXML
  private SectionStateView factoryState;
  @FXML
  private SectionStateView accessoriesState;
  @FXML
  private TextArea logsPool;

  private MainContext context = null;
  private Stage primaryStage;

  @FXML
  private void initialize() throws IOException {
    Properties sparePartsProperties = null;
    Properties controllerProperties = null;
    try (InputStream in = getClass().getResourceAsStream("spare_part.properties")) {
      sparePartsProperties = new Properties();
      sparePartsProperties.load(in);
    }
    try (InputStream in = getClass().getResourceAsStream("context.properties")) {
      controllerProperties = new Properties();
      controllerProperties.load(in);
    }

    factoryState.addListener(
            (observable, oldValue, newValue) -> context.getFactoryModel().setDelay(newValue.intValue())
    );
    engineState.addListener(
            (observable, oldValue, newValue) -> context.getEngineModel().setDelay(newValue.intValue())
    );
    bodyState.addListener(
            (observable, oldValue, newValue) -> context.getBodyModel().setDelay(newValue.intValue())
    );
    accessoriesState.addListener(
            (observable, oldValue, newValue) -> context.getAccessoriesModel().setDelay(newValue.intValue())
    );
    this.context = new MainContext(sparePartsProperties, controllerProperties);
    context.getBodyModel().getProvider().addProduceMonitorListener(
            condition -> Platform.runLater(() -> bodyState.setCondition(condition))
    );
    context.getEngineModel().getProvider().addProduceMonitorListener(
            condition -> Platform.runLater(() -> engineState.setCondition(condition))
    );
    context.getAccessoriesModel().getProvider().addProduceMonitorListener(
            condition -> Platform.runLater(() -> accessoriesState.setCondition(condition))
    );
    context.getFactoryModel().getFactory().addProduceMonitorListener(
            condition -> Platform.runLater(() -> factoryState.setCondition(condition))
    );
    this.context.perform();
  }


  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setOnCloseRequest(event -> {
      context.shutdown();
      Platform.exit();
    });
  }
}