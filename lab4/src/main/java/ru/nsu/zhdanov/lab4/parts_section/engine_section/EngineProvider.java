package ru.nsu.zhdanov.lab4.parts_section.engine_section;

import ru.nsu.zhdanov.lab4.parts_section.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineProvider extends SparePartProvider<Engine> {
  public EngineProvider(final AtomicInteger delay) {
    super(1, delay);
  }
}
