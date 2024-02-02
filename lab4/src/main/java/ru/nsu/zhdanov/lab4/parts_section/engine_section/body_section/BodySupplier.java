package ru.nsu.zhdanov.lab4.parts_section.engine_section.body_section;

public interface BodySupplier {
  abstract Body getBody() throws InterruptedException;
}
