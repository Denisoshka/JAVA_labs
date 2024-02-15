package ru.nsu.zhdanov.lab_3.game_context;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameEngine extends GameContext {
  public enum EngineInput {
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

    EngineInput(String name) {
      this.name = name;
    }
  }
  //  todo наверное можно булевые значения одни на стеке иметь на 2 места
  private Map<EngineInput, AtomicBoolean> input;

  GameEngine(Properties mapProperties) {
    super(mapProperties);
  }

  public Map<EngineInput, AtomicBoolean> getInput() {
    return input;
  }
}
