package ru.nsu.zhdanov.lab_4.facade;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.nsu.zhdanov.lab_4.model.SparePartSectionController;

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

  @FXML
  private void initialize() throws IOException {
    log.info("init MainController");
    Properties sparePartsProperties = null;
    Properties controllerProperties = null;

    ClassLoader classLoader = MainController.class.getClassLoader();
    if (classLoader == null) {
      throw new RuntimeException("pizda ryly dorogoi");
    }

    try (InputStream in = classLoader.getResourceAsStream("spare_part.properties")) {
      sparePartsProperties = new Properties();
      sparePartsProperties.load(in);
    }
    try (InputStream in = classLoader.getResourceAsStream("context.properties")) {
      controllerProperties = new Properties();
      controllerProperties.load(in);
    }

    log.info("init MainContext");
    this.context = new MainContext(sparePartsProperties, controllerProperties);

    initSlider(bodySlider, bodyValueSlider, this.context.bodySectionController);
    initSlider(engineSlider, engineValueSlider, this.context.engineSectionController);
    initSlider(accessoriesSlider, accessoriesValueSlider, this.context.accessoriesSectionController);

    this.context.perform();
  }

  private void initSlider(@NotNull final Slider slider, @NotNull final TextField textField, final SparePartSectionController spController) {
    log.info("init slider " + spController.toString());
    textField.setText(String.valueOf(slider.getValue()));
    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      textField.setText(String.valueOf(newValue.intValue()));
      spController.setProviderDelay(newValue.intValue());
    });
  }


  private void initMainContextProperties(Properties controllerProperties, Properties sparePartsProperties) throws IOException {
    log.info("initMainContextProperties");
    ClassLoader classLoader = MainController.class.getClassLoader();
    if (classLoader == null) {
      throw new RuntimeException("pizda ryly dorogoi");
    }

    log.info("load sparePartsProperties");
    try (InputStream in = classLoader.getResourceAsStream("lab_4/src/main/resources/ru.nsu.zhdanov.lab_4.facade/context.properties")) {
      sparePartsProperties = new Properties();
      sparePartsProperties.load(in);
    }
    log.info("load controllerProperties");
    try (InputStream in = classLoader.getResourceAsStream("lab_4/src/main/resources/ru.nsu.zhdanov.lab_4.facade/spare_part_controllers.properties")) {
      controllerProperties = new Properties();
      controllerProperties.load(in);
    }
  }
}