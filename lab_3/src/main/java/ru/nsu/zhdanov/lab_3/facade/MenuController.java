package ru.nsu.zhdanov.lab_3.facade;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

public class MenuController implements SubControllerRequests {
  TableView<Score> scoreTable;
  ObservableList<Score> scoreStorage;
  @FXML
  private TableColumn<Score, String> name;
  @FXML
  private TableColumn<Score, Integer> score;

  @FXML
  public void startGame() {
  }

  @FXML
  public void exitGame() {
  }

  @Override
  public void setContext(Properties properties) {
    try {
      var resource = getClass().getResource(properties.getProperty("score.json"));
      var mapper = new ObjectMapper();
      Score[] tmp = mapper.readValue(resource, Score[].class);
      Arrays.sort(tmp); //, Collections.reverseOrder()
      scoreStorage.addAll(tmp);
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException(e);
    }
    scoreTable.setItems(scoreStorage);
  }

  public record Score(String name, int score) implements Comparable<Score> {
    @Override
    public int compareTo(Score o) {
      return Integer.compare(this.score, o.score);
    }
  }
}
