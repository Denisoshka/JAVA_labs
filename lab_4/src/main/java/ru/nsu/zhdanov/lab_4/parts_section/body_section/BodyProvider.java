package ru.nsu.zhdanov.lab_4.parts_section.body_section;

import ru.nsu.zhdanov.lab_4.parts_section.SparePartProvider;

import java.util.concurrent.atomic.AtomicInteger;

public class BodyProvider extends SparePartProvider<Body> {
  public BodyProvider(final AtomicInteger delay) {
    super(1, delay);
  }
}
