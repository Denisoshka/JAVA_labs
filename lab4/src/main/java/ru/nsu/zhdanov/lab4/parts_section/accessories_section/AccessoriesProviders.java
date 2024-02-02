package ru.nsu.zhdanov.lab4.parts_section.accessories_section;

import ru.nsu.zhdanov.lab4.parts_section.SparePartFactory;
import ru.nsu.zhdanov.lab4.parts_section.SparePartProvider;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AccessoriesProviders implements SparePartSupplier<Accessories> {
  private final ArrayList<AccProv> providers;

  private final BlockingQueue<Accessories> accessories;

  AccessoriesProviders(final int quantity) {
    providers = new ArrayList<>(quantity);
    accessories = new ArrayBlockingQueue<>(quantity);
  }

  private void shutdown() {
    for (AccProv cell : providers) {
      cell.interrupt();
    }
  }

  public void start() {
    for (AccProv cell : providers) {
      cell.start();
    }
  }

  @Override
  synchronized public Accessories getSparePart() {
    try {
      while (accessories.isEmpty()) {
        wait();
      }
      Accessories acc = accessories.take();
      accessories.notifyAll();
      return acc;
    } catch (InterruptedException e) {
      return null;
    }
  }

  private class AccProv extends Thread {
    @Override
    public void run() {
      while (Thread.currentThread().isAlive()) {
        synchronized (accessories) {
          while (accessories.remainingCapacity() == 0) {
            try {
              wait();
            } catch (InterruptedException e) {
              return;
            }
          }
          try {
            accessories.put((Accessories) SparePartFactory.getInstance().newInstance(Accessories.getGetPartName()));
            accessories.notifyAll();
          } catch (InterruptedException e) {
            return;
          }
        }
      }
    }
  }
}
