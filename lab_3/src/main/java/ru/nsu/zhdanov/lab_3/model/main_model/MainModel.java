package ru.nsu.zhdanov.lab_3.model.main_model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.nsu.zhdanov.lab_3.model.game_context.GameEngine;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainModel {
  private final Properties properties;
  private String name = "unknown player";

  public MainModel(Properties properties) {
    this.properties = properties;
  }

  public List<String> acquireScore() {
    try {
      File resource = new File(properties.getProperty("score"));
      var mapper = new ObjectMapper();
      Score[] tmp = mapper.readValue(resource, Score[].class);
      Arrays.sort(tmp, Collections.reverseOrder());
      ArrayList<String> res = new ArrayList<>(tmp.length);
      for (var node : tmp){
        res.add(mapper.writeValueAsString(node));
      }
      return res;
    } catch (IOException | RuntimeException e) {
      throw new RuntimeException(e);
    }
  }

  public void dumpScore(Score score) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      File scoreFile = new File(properties.getProperty("score"));

      ArrayNode scores;
      if (scoreFile.exists()) {
        scores = (ArrayNode) objectMapper.readTree(scoreFile);
      } else {
        scores = objectMapper.createArrayNode();
      }

      scores.addPOJO(score);

      objectMapper.writeValue(scoreFile, scores);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setPlayerName(String name) {
    this.name = name == null || name.isEmpty() ? this.name : name;
  }

  public String getPlayerName() {
    return this.name;
  }

  public record Score(String name, int score) implements Comparable<Score> {
    @Override
    public int compareTo(Score o) {
      return Integer.compare(this.score, o.score);
    }
  }
}
