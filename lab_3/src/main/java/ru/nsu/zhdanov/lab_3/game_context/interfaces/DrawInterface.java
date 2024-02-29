package ru.nsu.zhdanov.lab_3.game_context.interfaces;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;

public interface DrawInterface {
  abstract public void draw(ContextID id, int x0, int y0, int width, int height, double cos, double sin, boolean reflect);
}
