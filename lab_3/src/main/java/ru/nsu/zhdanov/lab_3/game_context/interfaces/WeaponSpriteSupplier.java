package ru.nsu.zhdanov.lab_3.game_context.interfaces;

public interface WeaponSpriteSupplier {
  abstract void drawWeaponSprite(DrawInterface drawContext, int x, int y, double cosDir, double sinDir, boolean reflect);
}
