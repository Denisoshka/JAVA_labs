package ru.nsu.zhdanov.subtask5.subtask5.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import ru.nsu.zhdanov.subtask5.subtask5.model.Model;

import java.util.concurrent.atomic.AtomicInteger;

public class SupplierConsumerController {
  public TextField storageCapacity;
  public TextField consumersQuantity;
  public TextField suppliersQuantity;
  private Model model;
  AtomicInteger delay;
  @FXML
  private Slider slider;

  @FXML
  private Label valueLabel;

  @FXML
  public void initialize() {
    valueLabel.setText("Suppliers delay: " + (int) slider.getValue());

    slider.valueProperty().addListener((observable, oldValue, newValue) ->
    {
      valueLabel.setText("Suppliers delay: " + newValue.intValue());
      this.delay.set(newValue.intValue());
    });

  }

  @FXML
  public void start() {
    storageCapacity.setEditable(false);
    consumersQuantity.setEditable(false);
    suppliersQuantity.setEditable(false);
    int stCp = Integer.parseInt(storageCapacity.getText());
    int coCp = Integer.parseInt(consumersQuantity.getText());
    int suCp = Integer.parseInt(suppliersQuantity.getText());
    model = new Model(stCp, coCp, suCp);
  }
}