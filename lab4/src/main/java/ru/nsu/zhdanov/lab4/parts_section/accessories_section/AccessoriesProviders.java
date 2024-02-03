package ru.nsu.zhdanov.lab4.parts_section.accessories_section;

import ru.nsu.zhdanov.lab4.parts_section.SparePartFactory;
import ru.nsu.zhdanov.lab4.parts_section.SparePartProvider;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoriesProviders extends SparePartProvider<Accessories> {
  private final BlockingQueue<Accessories> accessories;
  {
    task = () -> {
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
            accessories.put((Accessories) SparePartFactory.getInstance().newInstance(Accessories.getPartName()));
            accessories.notifyAll();
          } catch (InterruptedException e) {
            return;
          }
        }
      }
    };
  }

  public AccessoriesProviders(final int providersQuantity, final AtomicInteger delay) {
    super(providersQuantity, delay);
    accessories = new ArrayBlockingQueue<>(providersQuantity);
//    todo хули ты ебешь мне мозг дура
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

}
