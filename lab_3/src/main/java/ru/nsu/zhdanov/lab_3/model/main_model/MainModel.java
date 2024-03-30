package ru.nsu.zhdanov.lab_3.model.main_model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MainModel {
  private final Properties properties;
  private String name = "unknown player";

  public MainModel() {
    Properties properties = new Properties();
    try {
      properties.load(Objects.requireNonNull(getClass().getResourceAsStream("main_model.properties")));
    } catch (NullPointerException | IOException e) {
      throw new RuntimeException("unable to load main_model.properties", e);
    }
    this.properties = properties;
  }

  public List<Score> acquireScore() {
    try {
      File resource = new File(properties.getProperty("score"));
      var mapper = new ObjectMapper();
      return mapper.<ArrayList<Score>>readValue(resource, new TypeReference<>() {
      });
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

  public static class Score implements Comparable<Score> {
    private final String name;
    private final int score;

    @JsonCreator
    public Score(@JsonProperty("name") String name, @JsonProperty("score") int score) {
      this.name = name;
      this.score = score;
    }

    public int getScore() {
      return score;
    }

    public String getName() {
      return name;
    }

    @Override
    public int compareTo(Score o) {
      return Integer.compare(this.score, o.score);
    }
  }
}
