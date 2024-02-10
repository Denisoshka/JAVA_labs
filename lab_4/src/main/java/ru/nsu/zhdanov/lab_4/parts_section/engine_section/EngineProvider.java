package ru.nsu.zhdanov.lab_4.parts_section.engine_section;

import ru.nsu.zhdanov.lab_4.parts_section.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineProvider extends SparePartProvider {
  public EngineProvider(final AtomicInteger delay) {
    super("ENGINE", 1, delay);
  }
}
