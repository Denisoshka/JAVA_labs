package ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.melee_weapon;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameContext;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.MeleeWeapon;

import static ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.Constants.AxeC;

@Slf4j
public class Axe extends MeleeWeapon implements AxeC {
  AreaOfDefeat area;

  public Axe() {
    super(ContextID.Axe, AXE_DAMAGE, ACTION_DISTANCE);
  }


  @Override
  public void action(GameContext context, PlayerController user) {
    log.info("am going ot init hurt");
    int udx = context.getCursorXPos() - user.getX();
    int udy = context.getCursorYPos() - user.getY();

    if (udx > 0 && udy < udx && udy > -udx) {
      area = AreaChecker.checkForward;
    } else if (udx < 0 && udy < -udx && udy > udx) {
      area = AreaChecker.checkBehind;
    } else if (udy > 0 && udy > udx && udy > -udx) {
      area = AreaChecker.checkTop;
    } else {
      area = AreaChecker.checkBottom;
    }

    for (Entity ent : context.getEntities()) {
      int edx = context.getCursorXPos() - ent.getX();
      int edy = context.getCursorYPos() - ent.getY();
      if (area.hits(edx, edy)) {
        int dx = user.getX() - ent.getX();
        int dy = user.getY() - ent.getY();
        if (Math.sqrt(dx * dx + dy * dy) < ACTION_DISTANCE) {
          ent.acceptDamage(this);
        }
      }
    }
  }

  @Override
  public int getDamage() {
    return super.getDamage();
  }


  @Override
  public void update(GameContext context, PlayerController user) {
  }
}
