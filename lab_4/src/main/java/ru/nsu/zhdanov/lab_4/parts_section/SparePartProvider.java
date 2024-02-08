package ru.nsu.zhdanov.lab_4.parts_section;

import lombok.Setter;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SparePartProvider<SparePartT extends SparePart> {
  protected AtomicInteger delay;
  //  todo сделать здесь какой то контроллек который позволяет менять извне
  protected final ArrayList<Thread> providers;
  protected @Setter SparePartConsumer<SparePartT> repository;
  protected Runnable task = () -> {
    while (Thread.currentThread().isAlive()) {
//      todo что то не так
      try {
        repository.acceptCar((SparePartT) SparePartFactory.getInstance().newInstance(SparePartT.partName));
        Thread.sleep(delay.get());
      } catch (InterruptedException e) {
        return;
      }
    }
  };

  public SparePartProvider(final int providersQuantity, final AtomicInteger delay) {
    this.providers = new ArrayList<>(providersQuantity);
    this.delay = delay;
    for (ListIterator<Thread> it = providers.listIterator(); it.hasNext(); it.next()) {
      it.set(new Thread(task));
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