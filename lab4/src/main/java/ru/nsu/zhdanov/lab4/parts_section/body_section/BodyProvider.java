package ru.nsu.zhdanov.lab4.parts_section.body_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;

import java.util.concurrent.atomic.AtomicBoolean;

public class BodyProvider implements Runnable, SparePartSupplier<Body> {
  @Setter
  private BodyRepository rep;
  @Setter
  private int delay;
  private AtomicBoolean bodyAvailable;

  public BodyProvider() {
  }

  @Override
  synchronized public Body getSparePart() {
    try {
      while (!bodyAvailable.get()) {
        wait();
      }
      bodyAvailable.set(false);
      return new Body();
    } catch (InterruptedException e) {
      return null;
    }
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      bodyAvailable.set(true);
      bodyAvailable.notifyAll();
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}