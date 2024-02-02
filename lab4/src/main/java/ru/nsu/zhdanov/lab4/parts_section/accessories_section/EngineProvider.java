package ru.nsu.zhdanov.lab4.parts_section.accessories_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineRepository;

import java.util.concurrent.atomic.AtomicBoolean;

public class EngineProvider implements Runnable, SparePartSupplier<Body> {
  @Setter
  private EngineRepository rep;
  @Setter
  private int delay;
  private AtomicBoolean EngineAvailable;

  public EngineProvider() {
  }

  @Override
  synchronized public Body getSparePart() {
    try {
      while (!EngineAvailable.get()) {
        wait();
      }
      EngineAvailable.set(false);
      return new Body();
    } catch (InterruptedException e) {
      return null;
    }
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      EngineAvailable.set(true);
      EngineAvailable.notifyAll();
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
