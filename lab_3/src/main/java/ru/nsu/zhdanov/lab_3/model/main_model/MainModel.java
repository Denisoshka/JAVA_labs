package ru.nsu.zhdanov.lab_3.model.main_model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.exception.DumpScoreException;
import ru.nsu.zhdanov.lab_3.model.exception.ResourceNotAvailable;

import java.io.*;
import java.util.*;

@Slf4j
public class MainModel {
  private final Properties properties;
  private String name = "unknown player";

  public MainModel() {
    Properties properties = new Properties();
    try {
      properties.load(Objects.requireNonNull(getClass().getResourceAsStream("main_model.properties")));
    } catch (NullPointerException | IOException e) {
      throw new ResourceNotAvailable("main_model.properties", e);
    }
    this.properties = properties;
  }

  public List<Score> acquireScore() {
    try {
      File resource = new File(properties.getProperty("score"));
      ObjectMapper mapper = new ObjectMapper();
      return (mapper.<ArrayList<Score>>readValue(resource, new TypeReference<>() {
      }));
    } catch (IOException e) {
      log.info("acq fail");
      return null;
    }
  }

  public void dumpScore(Score score) {
    List<Score> scoresList = Objects.requireNonNullElse(acquireScore(), new ArrayList<>());
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(properties.getProperty("score")))) {
      ObjectMapper objectMapper = new ObjectMapper();
      scoresList.add(score);
      Collections.sort(scoresList, Collections.reverseOrder());
      writer.write(objectMapper.writeValueAsString(scoresList));
    } catch (IOException e) {
      throw new DumpScoreException(e);
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
