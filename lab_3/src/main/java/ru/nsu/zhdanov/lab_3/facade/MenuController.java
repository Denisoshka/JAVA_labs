package ru.nsu.zhdanov.lab_3.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

@Slf4j
public class MenuController implements SubControllerRequests {
  @FXML
  private TableView<Line> scoreTable;
  @FXML
  private TableColumn<Line, String> nameColumn;
  @FXML
  private TableColumn<Line, Integer> scoreColumn;
  @FXML
  private TextField playerName;

  final private ObservableList<Line> scoreData;
  private MainControllerRequests.MenuContext menuReq;


  @FXML
  public void startGame() {
    log.info("try to start game");
    menuReq.startGame(playerName.getText());
  }

  public MenuController() {
    this.scoreTable = new TableView<>();
    this.scoreData = FXCollections.observableArrayList();
  }

  @FXML
  public void exitGame() {
    Platform.exit();
  }

  @Override
  public void setContext(Properties properties, MainController controller, Stage primaryStage) {
    menuReq = controller;
    ObjectMapper mapper = new ObjectMapper();
    List<String> rez = menuReq.acquireScore();
    try {
      for (var tmp : rez) {
        scoreData.add(mapper.readValue(tmp, Line.class));
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    scoreData.addAll();
    scoreTable.setItems(scoreData);
  }

  @Override
  public void perform() {
  }

  @Override
  public void shutdown() {
  }

  private record Line(SimpleStringProperty name, SimpleIntegerProperty score) {
  }
}
