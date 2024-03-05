package ru.nsu.zhdanov.lab_3.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.opposition.CycloDick;
import ru.nsu.zhdanov.lab_3.model.entity.player.Player;

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
  @Getter private int score = 0;


  public GameEngine(Properties contextProperties) {
    this.actionTraceBuffer = new ArrayList<>();
    this.contextProperties = contextProperties;
    this.obrservingQuantity = new AtomicInteger(0);
    //    todo make prop use
    this.entities = new ArrayList<>();
    this.input = new HashMap<>();
    this.initGameEnvironment();
  }

  void initGameEnvironment() {
    map = new GameMap(0, 0, testMapWidth, testMapHeight, 1000);
    player = new Player(300, 300, 0);
    curGameTime = new AtomicLong();


    Entity ent = new CycloDick(400, 400);
    ent.setContextTracker(obrservingQuantity);
    entities.add(ent);
  }

  public void perform() {
    gameStartTime = System.currentTimeMillis();
  }


  public void update() {//
    curGameTime.set(System.currentTimeMillis() - gameStartTime);
    player.update(this);

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

  private boolean gameIsEnd() {
    boolean condition;
    condition = player.isDead();
    if (obrservingQuantity.get() == 0) {

    }
    return condition;
  }

  private void saveScore() {
  }

  public Map<PlayerAction, AtomicBoolean> getInput() {
    return this.input;
  }

  public List<Entity> getDrawInf() {
    return entities;
  }
}
