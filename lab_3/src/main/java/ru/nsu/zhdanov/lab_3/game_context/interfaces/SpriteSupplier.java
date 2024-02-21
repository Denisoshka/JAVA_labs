package ru.nsu.zhdanov.lab_3.game_context.interfaces;

import ru.nsu.zhdanov.lab_3.game_context.ContextID;

public interface SpriteSupplier {
  abstract ContextID getSprite();

  abstract void drawSprite(DrawInterface drawContext);
//  abstract int getWidth();
//  abstract int getHeight();
}
