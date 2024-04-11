package ru.nsu.zhdanov.lab_4.model.factory.body_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class BodyProvider extends SparePartProvider<Body> {
  public BodyProvider(final AtomicInteger delay) {
    super("BODY", 1, delay);
  }
}
