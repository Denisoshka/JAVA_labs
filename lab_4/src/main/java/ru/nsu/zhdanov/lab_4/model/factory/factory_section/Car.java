package ru.nsu.zhdanov.lab_4.model.factory.factory_section;

import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;

import java.util.UUID;

public class Car {
  final Body body;
  final Engine engine;
  final Accessories acc;
  final private UUID id;

  public Car(Body body, Engine engine, Accessories acc) {
    this.body = body;
    this.engine = engine;
    this.acc = acc;
    this.id = UUID.randomUUID();
  }

  @Override
  public String toString() {
    return "Car " + id.toString() + " (" + body.toString() + " " + engine.toString() + " " + acc.toString() + ")";
  }
}
