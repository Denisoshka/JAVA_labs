package ru.nsu.zhdanov.lab_4.facade;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SectionStateView extends HBox {
  @FXML
  private TextField label;
  @FXML
  private TextField sliderValue;
  @FXML
  private TextField description;
  @FXML
  private TextField condition;
  @FXML
  private Slider slider;

  public TextField getQuantityLabel() {
    return quantityLabel;
  }

  public void setQuantityLabel(TextField quantityLabel) {
    this.quantityLabel = quantityLabel;
  }

  @FXML
  private TextField quantityLabel;

  public SectionStateView() {
    FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SectionStateView.fxml"));
    fxmlloader.setRoot(this);
    fxmlloader.setController(this);
    try {
      fxmlloader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }

    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      sliderValue.setText(String.valueOf(newValue.intValue()));
    });
  }

  public void addListener(ChangeListener<? super Number> listener) {
    slider.valueProperty().addListener(listener);
  }

  public double getMax() {
    return slider.maxProperty().get();
  }

  public DoubleProperty maxProperty() {
    return slider.maxProperty();
  }

  public void setMax(double max) {
    slider.maxProperty().set(max);
  }

  public double getMin() {
    return slider.minProperty().get();
  }

  public DoubleProperty minProperty() {
    return slider.minProperty();
  }

  public void setMin(double min) {
    slider.minProperty().set(min);
  }

  public String getLabel() {
    return label.textProperty().get();
  }

  public StringProperty labelProperty() {
    return label.textProperty();
  }

  public void setLabel(String label) {
    this.label.textProperty().set(label);
  }

  public String getDescription() {
    return description.textProperty().get();
  }

  public StringProperty descriptionProperty() {
    return description.textProperty();
  }

  public void setDescription(String name) {
    this.description.textProperty().set(name);
  }

  public String getCondition() {
    return condition.textProperty().get();
  }

  public StringProperty conditionProperty() {
    return condition.textProperty();
  }

  public void setCondition(String name) {
    this.condition.textProperty().set(name);
  }
}