package ru.nsu.zhdanov.lab_3.fx_facade;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.abstract_facade.SubControllerRequests;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import java.util.Properties;

@Slf4j
public class MenuController implements SubControllerRequests, FXControllerInterface {
  @FXML
  private TableView<MainModel.Score> scoreTable = new TableView<>();
  final private ObservableList<MainModel.Score> scoreData = FXCollections.observableArrayList();

  @FXML
  private TableColumn<MainModel.Score, String> nameColumn;
  @FXML
  private TableColumn<MainModel.Score, Integer> scoreColumn;
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

  public void setContext(Properties properties, MainController controller, Stage stage) {
    menuReq = controller;
    scoreData.addAll(menuReq.acquireScore());
  }

  @Override
  public void perform() {
  }

  @Override
  public void shutdown() {
  }

  /*public static class Line {
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
  }*/
}
