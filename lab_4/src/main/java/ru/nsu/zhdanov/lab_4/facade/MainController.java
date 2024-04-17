package ru.nsu.zhdanov.lab_4.facade;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.nsu.zhdanov.lab_4.model.SparePartSectionModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class MainController {
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
    this.context.perform();
  }

  private void initSlider(@NotNull final Slider slider, @NotNull final TextField textField, final SparePartSectionModel sparePartSectionController) {
    log.debug("init slider " + sparePartSectionController.toString());
    textField.setText(String.valueOf(slider.getValue()));
    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      log.debug("new delay" + sparePartSectionController + "=" + newValue.toString());
      textField.setText(String.valueOf(newValue.intValue()));
      sparePartSectionController.setProviderDelay(newValue.intValue());
    });
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }
}