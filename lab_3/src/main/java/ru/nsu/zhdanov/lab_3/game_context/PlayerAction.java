package ru.nsu.zhdanov.lab_3.game_context;

public enum PlayerAction {
  FORWARD("move forward"),
  LEFT("move left"),
  RIGHT("move right"),
  BACK("move back"),
  RELOAD("reload"),
  SHOOT("shoot");
  private final String name;

  public String getName() {
    return name;
  }

  PlayerAction(String name) {
    this.name = name;
  }
}
