package ru.nsu.zhdanov.lab_4.facade;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.nsu.zhdanov.lab_4.model.SparePartSectionModel;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SetDelayInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class MainController {
  @FXML
  private TextField factoryValueSlider;
  @FXML
  private Slider factorySlider;
  @FXML
  private TextArea logsPool;
  @FXML
  private Slider bodySlider;
  @FXML
  private TextField bodyValueSlider;
  @FXML
  private Slider engineSlider;
  @FXML
  private TextField engineValueSlider;
  @FXML
  private Slider accessoriesSlider;
  @FXML
  private TextField accessoriesValueSlider;

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
    this.context = new MainContext(sparePartsProperties, controllerProperties);
    initSlider(bodySlider, bodyValueSlider, this.context.getBodySectionModel());
    initSlider(engineSlider, engineValueSlider, this.context.getEngineSectionModel());
    initSlider(accessoriesSlider, accessoriesValueSlider, this.context.getAccessoriesSectionController());
    initSlider(factorySlider, factoryValueSlider, this.context.getFactoryModel());
    this.context.perform();
  }

  private void initSlider(@NotNull final Slider slider, @NotNull final TextField textField, final SetDelayInterface model) {
    log.debug("init slider " + model.toString());
    textField.setText(String.valueOf(slider.getValue()));
    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      log.debug("new delay" + model + "=" + newValue.toString());
      textField.setText(String.valueOf(newValue.intValue()));
      model.setDelay(newValue.intValue());
    });
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setOnCloseRequest(event -> {
      context.shutdown();
      Platform.exit();
    });
  }
}