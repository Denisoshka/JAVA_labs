package ru.nsu.zhdanov.lab_3.facade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
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
  private TableView<Line> scoreTable = new TableView<>();
  final private ObservableList<Line> scoreData = FXCollections.observableArrayList();

  @FXML
  private TableColumn<Line, String> nameColumn;
  @FXML
  private TableColumn<Line, Integer> scoreColumn;
  @FXML
  private TextField playerName;

  private MainControllerRequests.MenuContext menuReq;

  @FXML
  public void startGame() {
    menuReq.startGame(playerName.getText());
  }

  @FXML
  public void initialize() {
    scoreTable.setItems(scoreData);
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
        var a = mapper.readValue(tmp, Line.class);
        scoreData.add(a);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void perform() {
  }

  @Override
  public void shutdown() {
  }

  public static class Line {
    private String name;
    private int score;

    @JsonCreator
    public Line(@JsonProperty("name") String name, @JsonProperty("score") int score) {
      this.name = name;
      this.score = score;
    }

    public int getScore() {
      return score;
    }

    public String getName() {
      return name;
    }
  }
}
