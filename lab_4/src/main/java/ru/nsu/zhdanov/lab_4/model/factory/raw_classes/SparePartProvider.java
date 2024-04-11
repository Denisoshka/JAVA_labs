package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class SparePartProvider<SparePartT> {
  protected AtomicInteger delay;
  //  todo сделать здесь какой то контроллек который позволяет менять извне
  protected final Thread[] providers;
  protected @Setter SparePartConsumer<SparePartT> repository;
  protected final String sparePartName;
  protected final Runnable task;

  public SparePartProvider(final String sparePartName, final int providersQuantity, final AtomicInteger delay) {
    this.providers = new Thread[providersQuantity];
    this.delay = delay;
    this.sparePartName = sparePartName;
    this.task = () -> {
      while (Thread.currentThread().isAlive()) {
        try {
          log.info(sparePartName + " instance new sparePart ");
          SparePartT sparePart = (SparePartT) SparePartFactory.getInstance().newInstance(sparePartName);
          log.info("provide " + sparePartName);
          repository.acceptSparePart(sparePart);
          Thread.sleep(delay.get());
        } catch (InterruptedException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
          log.error("catch ex: " + e);
          return;
        }
      }
    };
    log.info(this.sparePartName + " init " + "providersQuantity: " + providersQuantity + ", " + "delay: " + delay);
    for (int i = 0; i < providersQuantity; i++) {
      this.providers[i] = new Thread(task);
    }
  }

  public void shutdownNow() {
    log.info(sparePartName + " shutdown");
    for (Thread cell : providers) {
      cell.interrupt();
    }
  }

  public void perform() {
    log.info(sparePartName + " perform");
    for (Thread cell : providers) {
      cell.start();
    }
  }
}