package ru.nsu.zhdanov.subtask5.subtask5.model;

import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Suppliers {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Suppliers.class);
  private final ProductSupplier[] workers;
  private final AtomicInteger delay;

  public Suppliers(int workersQuantity, AtomicInteger delayBind, Consumer<Product> consumer) {
    this.delay = delayBind;
    this.workers = new ProductSupplier[workersQuantity];
    for (int i = 0; i < workers.length; ++i) {
      workers[i] = new ProductSupplier(consumer);
    }
  }

  public void perform() {
    for (var worker : workers) {
      worker.perform();
    }
  }

  public void shutdown() {
    for (var worker : workers) {
      worker.shutdown();
    }
  }

  private class ProductSupplier {
    private static int ID = 0;
    private final int id;
    private final Thread task;

    ProductSupplier(Consumer<Product> consumer) {
      id = ID++;
      task = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
          consumer.accept(new Product(id));
          try {
            Thread.sleep(delay.get());
          } catch (InterruptedException e) {
            return;
          }
        }
      });
    }

    public void perform() {
      task.start();
    }

    public void shutdown() {
      task.interrupt();
    }
  }
}
