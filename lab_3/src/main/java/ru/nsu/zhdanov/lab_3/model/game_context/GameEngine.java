package ru.nsu.zhdanov.lab_3.model.game_context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition.CycloDick;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.Player;

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

  final SessionInf sessionInf;
  final Properties properties;
  final Random random = new Random();

  public GameEngine(Properties properties, String player) {
    this.actionTraceBuffer = new ArrayList<>();
    this.obrservingQuantity = new AtomicInteger(0);
    this.entities = new ArrayList<>();
    this.input = new HashMap<>();
    this.properties = properties;
    this.sessionInf = new SessionInf(player);

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

    updateEnt();

    updateGameCondition();
  }

  private void updateEnt() {
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
      sessionInf.score += entity.getReward();
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

  private void updateGameCondition() {
    sessionInf.gameIsEnd = player.isDead() || obrservingQuantity.get() == 0;
  }

  public boolean gameIsEnd() {
    return sessionInf.gameIsEnd;
  }

  public Map<PlayerAction, AtomicBoolean> getInput() {
    return this.input;
  }

  public String getPlayerName() {
    return sessionInf.name;
  }

  public int getPlayerScore() {
    return sessionInf.score;
  }

  private class SessionInf {
    final String name;
    boolean allEnemyDead = false;
    boolean playerDead = false;
    boolean playerQuit = false;
    boolean gameIsEnd = false;
    int score;

    public SessionInf(String name){
      this.name = name;
    }
  }
}
