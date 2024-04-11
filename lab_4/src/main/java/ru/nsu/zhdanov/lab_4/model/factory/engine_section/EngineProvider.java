package ru.nsu.zhdanov.lab_4.model.factory.engine_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineProvider extends SparePartProvider<Engine> {
  public EngineProvider(final AtomicInteger delay) {
    super("ENGINE", 1, delay);
  }
}
