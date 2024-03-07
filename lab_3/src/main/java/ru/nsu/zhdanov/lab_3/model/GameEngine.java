package ru.nsu.zhdanov.lab_3.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.opposition.CycloDick;
import ru.nsu.zhdanov.lab_3.model.entity.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class GameEngine {
  private @Getter GameMap map;
  private @Getter Player player;
  private final @Getter List<Entity> entities;
  private final @Getter List<Entity> actionTraceBuffer;
  private final Map<PlayerAction, AtomicBoolean> input;
  //  todo
  private Properties contextProperties;
  private @Setter
  @Getter int cursorXPos;
  private @Setter
  @Getter int cursorYPos;
  final private int testMapWidth = 1200;
  final private int testMapHeight = 675;

  final private AtomicInteger obrservingQuantity;
  private AtomicLong curGameTime;
  private long gameStartTime;
  @Getter
  private int score = 0;

  final SessionInf sessionInf = new SessionInf();
  final Properties properties;
  final Random random = new Random();

  public GameEngine(Properties properties) {
    this.actionTraceBuffer = new ArrayList<>();
    this.obrservingQuantity = new AtomicInteger(0);
    this.entities = new ArrayList<>();
    this.input = new HashMap<>();
    this.properties = properties;

    this.initGameEnvironment();
  }

  void initGameEnvironment() {
    map = new GameMap(0, 0, testMapWidth, testMapHeight);
    player = new Player(300, 300, 0);
    curGameTime = new AtomicLong();

    spawnEnt();
  }

  public void perform() {
    gameStartTime = System.currentTimeMillis();
  }


  public void update() {//
    curGameTime.set(System.currentTimeMillis() - gameStartTime);
    player.update(this);
    updateEnt();

    updateGameCondition();
  }

  private void updateEnt() {
    for (Entity ent : entities) {
      ent.update(this);
    }

    player.checkCollisions(this);
    for (Entity ent : entities) {
      ent.checkCollisions(this);
    }

    entities.removeIf((entity) -> {
      if (!entity.isDead()) {
        return false;
      }
      entity.shutdown();
      return true;
    });
    entities.addAll(actionTraceBuffer);
    actionTraceBuffer.clear();
  }

  private void spawnEnt() {
    int x = map.getAllowedXPlacement(random.nextInt(testMapWidth), CycloDick.RADIUS);
    int y = map.getAllowedYPlacement(random.nextInt(testMapHeight), CycloDick.RADIUS);

    Entity ent = new CycloDick(x, y);
    ent.setContextTracker(obrservingQuantity);

    entities.add(ent);
  }

  public void shutDown() {
    if (gameIsEnd()) {
      saveScore();
    }
  }

  private void saveScore() {
//    todo make save
   /* try {
      ObjectMapper objectMapper = new ObjectMapper();
      File scoreFile = new File(properties.getProperty("score"));

      ArrayNode scores;
      if (scoreFile.exists()) {
        scores = (ArrayNode) objectMapper.readTree(scoreFile);
      } else {
        scores = objectMapper.createArrayNode();
      }

      scores.add(objectMapper.writeValueAsString(new Score(sessionInf.name, sessionInf.score)));

      objectMapper.writeValue(scoreFile, scores);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }*/
  }

  private void updateGameCondition() {
    sessionInf.gameIsEnd = player.isDead() || obrservingQuantity.get() == 0;
  }

  public boolean gameIsEnd() {
    return sessionInf.gameIsEnd;
  }

  public Map<PlayerAction, AtomicBoolean> getInput() {
    return this.input;
  }

  private class SessionInf {
    String name;
    int score;
    boolean allEnemyDead = false;
    boolean playerDead = false;
    boolean playerQuit = false;
    boolean gameIsEnd = false;
  }

  private record Score(String name, int score) {
  }
}
