package ru.nsu.zhdanov.lab_3.game_context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.player.Player;
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class GameEngine {
  protected @Getter GameMap map;
  protected @Getter Player player;
  protected @Getter List<Entity> entities;
  protected Map<PlayerAction, AtomicBoolean> input;
  //  todo
  protected Properties contextProperties;
  protected @Getter
  @Setter double cameraSin;
  protected @Getter
  @Setter double cameraCos;
  protected double GameSceneWidth;
  protected double GameSceneHeight;

  public GameEngine(Properties contextProperties) {
    this.contextProperties = contextProperties;
//    todo make prop use
    this.player = new Player(0, 0, 10, 10);
    this.entities = new ArrayList<>();
    this.input = new HashMap<>();
    this.initMap();
    this.initGameEnvironment();
  }

  void initGameEnvironment() {
    map = new GameMap(0, 0, 600, 600, 1000);

  }

  public void update() {//
    player.update(this);
    for (Entity ent : entities) {
      ent.update(this);
    }

    player.checkCollisions(this);
    for (Entity ent : entities) {
      ent.checkCollisions(this);
    }

    entities.removeIf(Entity::isAlive);
  }

  public void drawScene(DrawInterface drawContext) {
    player.drawSprite(drawContext);
    for (Entity ent : entities) {
      ent.drawSprite(drawContext);
    }
  }

  public Map<PlayerAction, AtomicBoolean> getInput() {
    return this.input;
  }

  public List<Entity> getDrawInf() {
    return entities;
  }
}