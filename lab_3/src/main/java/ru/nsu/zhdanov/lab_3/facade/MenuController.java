package ru.nsu.zhdanov.lab_3.facade;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class MenuController implements SubControllerRequests {
  @FXML
  private TableView<Score> scoreTable;
  @FXML
  private TableColumn<Score, String> name;
  @FXML
  private TableColumn<Score, Integer> score;

  final private ObservableList<Score> scoreStorage;
  private MainControllerRequests.MenuContext menuReq;


  @FXML
  public void startGame() {
    log.info("try to start game");
    menuReq.startGame();
  }

  public MenuController() {
    this.scoreTable = new TableView<>();
    this.scoreStorage = FXCollections.observableArrayList();
  }

  @FXML
  public void exitGame() {
  }

  @FXML
  private void initialize() {
  }


  @Override
  public void setContext(Properties properties, MainController controller, Stage primaryStage) {
    menuReq = controller;
    try {
      var resource = getClass().getResource(properties.getProperty("score"));
      var mapper = new ObjectMapper();
      Score[] tmp = mapper.readValue(resource, Score[].class);
      Arrays.sort(tmp);
      scoreStorage.addAll(tmp);
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException(e);
    }
    scoreTable.setItems(scoreStorage);
  }

  @Override
  public void perform() {
  }

  @Override
  public void shutdown() {
  }

  public record Score(String name, int score) implements Comparable<Score> {
    @Override
    public int compareTo(Score o) {
      return Integer.compare(this.score, o.score);
    }
  }
}
