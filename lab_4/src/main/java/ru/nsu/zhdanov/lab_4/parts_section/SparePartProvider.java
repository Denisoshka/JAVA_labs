package ru.nsu.zhdanov.lab_4.parts_section;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class SparePartProvider<SparePartT> {
  protected AtomicInteger delay;
  //  todo сделать здесь какой то контроллек который позволяет менять извне
  protected final List<Thread> providers;
  protected @Setter SparePartConsumer<SparePartT> repository;
  protected final String sparePartName;
  protected final Runnable task;

  public SparePartProvider(final String sparePartName, final int providersQuantity, final AtomicInteger delay) {
    this.providers = new ArrayList<>(providersQuantity);
    this.delay = delay;
    this.sparePartName = sparePartName;
    this.task = () -> {
      while (Thread.currentThread().isAlive()) {
//      todo что то не так
        try {
          log.info(sparePartName + " instance new sparePart ");
          SparePartT sparePart = (SparePartT) SparePartFactory.getInstance().newInstance(sparePartName);
          log.info("provide " + sparePartName);
          repository.acceptSparePart(sparePart);
          Thread.sleep(delay.get());
        } catch (InterruptedException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
          log.error("catch ex: " + e);
        }
      }
    };
    log.info(this.sparePartName + " init " + "providersQuantity: " + providersQuantity + ", " + "delay: " + delay);
    log.info("providersQuantity " + this.providers.size());
    for (int i = 0; i < providersQuantity; i++) {
      log.info(sparePartName + " set task " + i);
      this.providers.add(new Thread(task));
    }
  }

  public void shutdown() {
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