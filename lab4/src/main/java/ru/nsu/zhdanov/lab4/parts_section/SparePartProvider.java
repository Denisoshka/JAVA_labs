package ru.nsu.zhdanov.lab4.parts_section;

import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SparePartProvider<SparePartT extends SparePart> implements Runnable, SparePartSupplier<SparePartT> {
  @Setter
  private AtomicInteger delay;
  private final AtomicBoolean EngineAvailable;

  public SparePartProvider() {
    EngineAvailable = new AtomicBoolean(true);
  }

  @Override
  synchronized public SparePartT getSparePart() {
    try {
      while (!EngineAvailable.get()) {
        wait();
      }
      return (SparePartT) SparePartFactory.getInstance().newInstance(SparePartT.getPartName);
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
        Thread.sleep(delay.get());
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
