package ru.nsu.zhdanov.lab_4.facade;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainController {
  private final Logger log = org.slf4j.LoggerFactory.getLogger(MainController.class.getName());
//  private final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

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

  private MainContext modelContext = null;
  private Stage primaryStage;

  @FXML
  private void initialize() throws IOException {
    Properties sparePartsProperties;
    Properties controllerProperties;
    try (InputStream in = getClass().getResourceAsStream("spare_part.properties")) {
      sparePartsProperties = new Properties();
      sparePartsProperties.load(in);
    }
    try (InputStream in = getClass().getResourceAsStream("context.properties")) {
      controllerProperties = new Properties();
      controllerProperties.load(in);
    }

    factoryState.addListener(
            (observable, oldValue, newValue) -> modelContext.getFactoryModel().setDelay(newValue.intValue())
    );
    engineState.addListener(
            (observable, oldValue, newValue) -> modelContext.getEngineModel().setDelay(newValue.intValue())
    );
    bodyState.addListener(
            (observable, oldValue, newValue) -> modelContext.getBodyModel().setDelay(newValue.intValue())
    );
    accessoriesState.addListener(
            (observable, oldValue, newValue) -> modelContext.getAccessoriesModel().setDelay(newValue.intValue())
    );
    this.modelContext = new MainContext(sparePartsProperties, controllerProperties);
    modelContext.getBodyModel().getProvider().addProduceMonitorListener(
            condition -> Platform.runLater(() -> bodyState.setCondition(condition))
    );
    modelContext.getEngineModel().getProvider().addProduceMonitorListener(
            condition -> Platform.runLater(() -> engineState.setCondition(condition))
    );
    modelContext.getAccessoriesModel().getProvider().addProduceMonitorListener(
            condition -> Platform.runLater(() -> accessoriesState.setCondition(condition))
    );
    modelContext.getFactoryModel().getFactory().addProduceMonitorListener(
            condition -> Platform.runLater(() -> factoryState.setCondition(condition))
    );

    if (Boolean.parseBoolean((String) controllerProperties.getOrDefault("logSale", "false"))) {
      modelContext.getDealerModel().getDealer().addCarProduceListener((dealer, car) -> log.info(dealer + " " + car.toString()));
    }

    this.modelContext.perform();
  }


  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setOnCloseRequest(event -> {
      modelContext.shutdown();
      Platform.exit();
    });
  }
}