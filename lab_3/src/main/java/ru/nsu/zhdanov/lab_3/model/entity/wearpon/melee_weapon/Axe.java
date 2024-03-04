package ru.nsu.zhdanov.lab_3.model.entity.wearpon.melee_weapon;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.wearpon.base_weapons.MeleeWeapon;

@Slf4j
public class Axe extends MeleeWeapon {
  private static int ACTIONDISTANCE = 40;
  private static int AXEDAMAGE = 20;
  AreaOfDefeat area;

  public Axe() {
    super(ContextID.Axe, AXEDAMAGE, ACTIONDISTANCE);
  }

  @Override
  public void action(GameEngine context, Entity user) {
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
        if (Math.sqrt(dx * dx + dy * dy) < ACTIONDISTANCE) {
          ent.acceptDamage(this);
        }
      }
    }
  }

  @Override
  public int getDamage() {
    return super.getDamage();
  }

}
