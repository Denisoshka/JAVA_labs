package ru.nsu.zhdanov.lab_4.model.factory.engine_section;

import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class EngineProvider extends SparePartProvider<Engine> {
  public EngineProvider(final SparePartFactoryInterface factory, final int delay) {
    super(factory, SparePartType.ENGINE, 1, delay);
  }
}
