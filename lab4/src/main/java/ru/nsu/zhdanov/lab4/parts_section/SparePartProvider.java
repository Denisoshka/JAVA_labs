package ru.nsu.zhdanov.lab4.parts_section;

import lombok.Setter;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SparePartProvider<SparePartT extends SparePart> implements SparePartSupplier<SparePartT> {
  protected AtomicInteger delay;
  //  todo сделать здесь какой то контроллек который позволяет менять извне
  protected final AtomicBoolean SparePartAvailable;
  protected final ArrayList<Thread> providers;
  protected Runnable task = () -> {
    while (Thread.currentThread().isAlive()) {
//      todo что то не так
      SparePartAvailable.set(true);
      SparePartAvailable.notifyAll();
      try {
        Thread.sleep(delay.get());
      } catch (InterruptedException e) {
        return;
      }
    }
  };

  public SparePartProvider(final int providersQuantity, final AtomicInteger delay) {
    this.providers = new ArrayList<>(providersQuantity);
    this.SparePartAvailable = new AtomicBoolean(true);
    this.delay = delay;
    for (ListIterator<Thread> it = providers.listIterator(); it.hasNext(); it.next()) {
      it.set(new Thread(task));
    }
  }

  @Override
  synchronized public SparePartT getSparePart() {
    try {
      while (!SparePartAvailable.get()) {
        wait();
      }
      return (SparePartT) SparePartFactory.getInstance().newInstance(SparePartT.partName);
    } catch (InterruptedException e) {
      return null;
    }
  }

  public void shutdown() {
    for (Thread cell : providers) {
      cell.interrupt();
    }
  }

  public void perform() {
    for (Thread cell : providers) {
      cell.start();
    }
  }
}