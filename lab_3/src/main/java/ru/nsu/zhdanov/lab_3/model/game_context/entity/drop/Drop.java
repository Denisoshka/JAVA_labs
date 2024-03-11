package ru.nsu.zhdanov.lab_3.model.game_context.entity.drop;

import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Fraction;

import static ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextType.DropT;

public class Drop extends Entity {
  private int quantity;
  public Drop(int x, int y, int radius, ContextID ID, Fraction fraction) {
    super(x, y, radius, 0, 0, 0, 1, DropT, ID, fraction);
  }

  @Override
  public void update(GameContext context) {

  }

  @Override
  public void checkCollisions(GameContext context) {

  }
}
