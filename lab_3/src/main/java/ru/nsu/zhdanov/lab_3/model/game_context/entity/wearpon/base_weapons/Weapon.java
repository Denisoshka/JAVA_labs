package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons;

import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.interfaces.WeaponImpl;

public abstract class Weapon implements WeaponImpl {
  protected ContextID ID;
  private int damage;

  public Weapon(ContextID ID, int damage) {
    this.ID = ID;
    this.damage = damage;
  }

  abstract public void update(GameSession context, PlayerController user);

  abstract public void action(GameSession context, PlayerController user);

  public ContextID getID() {
    return this.ID;
  }

  public int getDamage() {
    return this.damage;
  }
}
