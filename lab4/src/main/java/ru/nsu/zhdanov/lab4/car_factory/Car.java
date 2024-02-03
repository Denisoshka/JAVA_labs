package ru.nsu.zhdanov.lab4.car_factory;

import lombok.ToString;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.Accessories;
import ru.nsu.zhdanov.lab4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine;

import java.util.UUID;

public class Car implements CarSupplier<Car> {
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
