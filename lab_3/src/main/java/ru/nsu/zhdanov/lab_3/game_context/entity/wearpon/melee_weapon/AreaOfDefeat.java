package ru.nsu.zhdanov.lab_3.game_context.entity.wearpon.melee_weapon;

import java.util.function.BiPredicate;

public interface AreaOfDefeat {
  abstract boolean hits(int dx, int dy);
}


