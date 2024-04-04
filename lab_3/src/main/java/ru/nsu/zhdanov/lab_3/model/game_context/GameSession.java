package ru.nsu.zhdanov.lab_3.model.game_context;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition.CycloDick;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition.TwoBarrels;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession {
  private GameMap map;
  private PlayerController player;
  private final List<Entity> entities = new ArrayList<>();
  private final List<Entity> actionTraceBuffer = new ArrayList<>();
  private final Map<PlayerAction, AtomicBoolean> inputTrace = new HashMap<>();

  private int cursorXPos;
  private int cursorYPos;
  private final int testMapWidth = 1200;
  private final int testMapHeight = 675;
  private final AtomicInteger observingQuantity = new AtomicInteger(0);

  private final Random random = new Random();

  private final String playerName;
  private long gameStartTime;

  private long lastSpawnTime;
  private int score;
  private long spawnDelay = 4000;
  private double delayReduceCoef = 0.9;

  private final static long timeToUpdate = 17;
  private final IOProcessing IOGameSessionHandle;
  private Thread session;

  public GameSession(Properties properties, IOProcessing IOHandle, String playerName) {
    this.IOGameSessionHandle = IOHandle;
    this.playerName = playerName;
    this.initGameEnvironment();
  }

  void initGameEnvironment() {
    map = new GameMap(0, 0, testMapWidth, testMapHeight);
    player = new PlayerController(300, 300);
    this.session = new Thread(() -> {
      long start, end, diff;
      while (!isGameEnd()) {
        start = System.currentTimeMillis();

        IOGameSessionHandle.handleInput();
        this.update();

        try {
          IOGameSessionHandle.handleOutput();
        } catch (InterruptedException e) {
          return;
        }

        end = System.currentTimeMillis();

        if ((diff = end - start) < timeToUpdate) {
          try {
            Thread.sleep(timeToUpdate - (diff));
          } catch (InterruptedException e) {
            return;
          }
        }
      }
      IOGameSessionHandle.signalGameEnd();
    });
  }

  public void perform() {
    gameStartTime = System.currentTimeMillis();
    lastSpawnTime = gameStartTime;
    session.start();
  }

  public void shutdown() {
    session.interrupt();
  }

  public void update() {//
    updateEnt();
    updateContext();
  }

  private void updateContext() {
    long time = System.currentTimeMillis();
    if (time - lastSpawnTime < spawnDelay) {
      return;
    }
    if (random.nextInt(4) % 3 == 0) {
      spawnCycloDick();
    } else {
      spawnTwoBarrels();
    }
    lastSpawnTime = time;
    if (spawnDelay >= 2000) {
      spawnDelay = (long) (spawnDelay * delayReduceCoef);
    }
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
      this.score += entity.getReward();
      return true;
    });

    entities.addAll(actionTraceBuffer);
    actionTraceBuffer.clear();
  }


  private void spawnCycloDick() {
    int x = map.getAllowedXPlacement(random.nextInt(testMapWidth), CycloDick.RADIUS);
    int y = map.getAllowedYPlacement(random.nextInt(testMapHeight), CycloDick.RADIUS);

    Entity ent = new CycloDick(x, y);
    ent.setContextTracker(observingQuantity);

    entities.add(ent);
  }

  private void spawnTwoBarrels() {
    int x = map.getAllowedXPlacement(random.nextInt(testMapWidth), CycloDick.RADIUS);
    int y = map.getAllowedYPlacement(random.nextInt(testMapHeight), CycloDick.RADIUS);

    Entity ent = new TwoBarrels(x, y);
    ent.setContextTracker(observingQuantity);

    entities.add(ent);
  }


  public void submitAction(Entity entity) {
    actionTraceBuffer.add(entity);
  }

  public boolean isGameEnd() {
    return player.isDead();
  }

  public ShootingWeapon getWeapon() {
    return player.getWeapon();
  }

  public int getWeaponOccupancy() {
    return player.getWeapon().getOccupancy();
  }

  public Map<PlayerAction, AtomicBoolean> getInputTrace() {
    return inputTrace;
  }

  public int getWeaponCapacity() {
    return player.getWeapon().getCapacity();
  }

  public int getScore() {
    return score;
  }

  public String getPlayerName() {
    return playerName;
  }

  public GameMap getMap() {
    return this.map;
  }

  public PlayerController getPlayer() {
    return this.player;
  }

  public List<Entity> getEntities() {
    return this.entities;
  }

  public int getCursorXPos() {
    return this.cursorXPos;
  }

  public int getCursorYPos() {
    return this.cursorYPos;
  }

  public void setCursorXPos(int cursorXPos) {
    this.cursorXPos = cursorXPos;
  }

  public void setCursorYPos(int cursorYPos) {
    this.cursorYPos = cursorYPos;
  }
}
