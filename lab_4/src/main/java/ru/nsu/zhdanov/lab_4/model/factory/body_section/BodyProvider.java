package ru.nsu.zhdanov.lab_4.model.factory.body_section;

import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class BodyProvider extends SparePartProvider<Body> {
  public BodyProvider(final SparePartFactoryInterface factory, final int delay) {
    super(factory, SparePartType.BODY, 1, delay);
  }
}
