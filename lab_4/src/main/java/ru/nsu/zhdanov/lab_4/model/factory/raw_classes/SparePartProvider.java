package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class SparePartProvider<SparePartT> {
  protected @Setter SparePartConsumer<SparePartT> repository;
  private final SparePartFactoryInterface factory;

  private volatile int delay;
  private final ExecutorService executorService;
  private final SparePartType sparePartName;
  private final int providersQuantity;

  public SparePartProvider(final SparePartFactoryInterface factory, final SparePartType type, final int providersQuantity, final int delay) {
    this.executorService = Executors.newFixedThreadPool(providersQuantity);
    this.providersQuantity = providersQuantity;
    this.sparePartName = type;
    this.factory = factory;
    this.delay = delay;
    log.info(this.sparePartName + " init " + "providersQuantity: " + providersQuantity + ", " + "delay: " + delay);
  }

  public void perform() {
    Runnable task = () -> {
      while (Thread.currentThread().isAlive()) {
        try {
          log.info(sparePartName + " instance new sparePart ");
          SparePartT sparePart = (SparePartT) factory.make(sparePartName.toString());
          log.info("provide " + sparePartName);
          repository.acceptSparePart(sparePart);
          Thread.sleep(delay);
        } catch (InterruptedException e) {
          log.error("shutdown: " + e);
          return;
        }
      }
    };
    for (int i = 0; i < providersQuantity; i++) {
      executorService.submit(task);
    }
    log.info(sparePartName + " perform");
  }

  public void shutdownNow() {
    executorService.shutdownNow();
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public SparePartType getSparePartName() {
    return sparePartName;
  }
}