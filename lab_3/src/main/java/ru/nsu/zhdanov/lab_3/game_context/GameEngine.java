package ru.nsu.zhdanov.lab_3.game_context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.opposition.CycloDick;
import ru.nsu.zhdanov.lab_3.game_context.entity.player.Player;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class GameEngine {
  private @Getter GameMap map;
  private @Getter Player player;
  private final @Getter  List<Entity> entities;
  private final Map<PlayerAction, AtomicBoolean> input;
  //  todo
  private Properties contextProperties;
  //  private double cameraSin;
//  private double cameraCos;
  private @Setter
  @Getter int cursorXPos;
  private @Setter
  @Getter int cursorYPos;
  final private int testMapWidth = 1200;
  final private int testMapHeight = 675;
  private int GameSceneWidth;
  private int GameSceneHeight;

  public GameEngine(Properties contextProperties) {
    this.contextProperties = contextProperties;
//    todo make prop use
    this.entities = new ArrayList<>();
    this.input = new HashMap<>();
    this.initGameEnvironment();
  }

  void initGameEnvironment() {
    map = new GameMap(0, 0, testMapWidth, testMapHeight, 1000);
    player = new Player(300, 300, 10, 0);
    entities.add(new CycloDick(400, 400));
  }

  public void update() {//
    player.update(this);
//    todo не терпит чтобы еще кто то другой изменял его лол
    for (Entity ent : entities) {
      ent.update(this);
    }

    player.checkCollisions(this);
    for (Entity ent : entities) {
      ent.checkCollisions(this);
    }

//    entities.removeIf(Entity::isAlive);
  }

  public void drawScene(DrawInterface drawContext) {
    drawContext.draw(map.getID(), 0, 0, testMapWidth, testMapHeight, 0, 0, false);
    for (Entity ent : entities) {
//      log.info(ent.getClass().getName());
      ent.drawEntitySprite(drawContext);
    }
    drawPlayer(drawContext);
  }


  private void drawPlayer(DrawInterface drawContext){
    player.drawEntitySprite(drawContext);
  }


  public Map<PlayerAction, AtomicBoolean> getInput() {
    return this.input;
  }

  public List<Entity> getDrawInf() {
    return entities;
  }
}
