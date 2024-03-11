package ru.nsu.zhdanov.lab_3.model.game_context;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition.CycloDick;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.opposition.TwoBarrels;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.ShootingWeapon;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class GameContext {
  private GameMap map;
  private PlayerController player;
  private final List<Entity> entities = new ArrayList<>();
  private final List<Entity> actionTraceBuffer = new ArrayList<>();
  private final Map<PlayerAction, AtomicBoolean> input = new HashMap<>();

  private int cursorXPos;
  private int cursorYPos;
  final private int testMapWidth = 1200;
  final private int testMapHeight = 675;
  final private AtomicInteger observingQuantity = new AtomicInteger(0);

  private final Random random = new Random();

  private String playerName;
  private long curGameTime;
  private long gameStartTime;


  private int score;

  public GameContext(Properties properties, String playerName) {
    this.playerName = playerName;
    this.initGameEnvironment();
  }

  void initGameEnvironment() {
    map = new GameMap(0, 0, testMapWidth, testMapHeight);
    player = new PlayerController(300, 300);

    spawnCycloDick();
    spawnCycloDick();
    spawnTwoBarrels();
  }

  public void perform() {
    gameStartTime = System.currentTimeMillis();
  }

  public void update() {//
    curGameTime = System.currentTimeMillis() - gameStartTime;

    updateEnt();
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
    return observingQuantity.get() == 0 || player.isDead();
  }

  public ShootingWeapon getWeapon() {
    return player.getWeapon();
  }

  public int getWeaponOccupancy() {
    return player.getWeapon().getOccupancy();
  }

  public Map<PlayerAction, AtomicBoolean> getInput() {
    return input;
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
