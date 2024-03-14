package ru.nsu.zhdanov.subtask5.subtask5.model;

import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Consumers {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Suppliers.class);
  private final ProductConsumer[] workers;

  public Consumers(int workersQuantity, Supplier<Product> supplier) {
    this.workers = new ProductConsumer[workersQuantity];
    for (int i = 0; i < workers.length; ++i) {
      workers[i] = new ProductConsumer(supplier);
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

  private static class ProductConsumer {
    private static int ID = 0;
    private final int id;
    private final Thread task;

    ProductConsumer(Supplier<Product> supplier) {
      id = ID++;
      task = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
          Product tmp = supplier.get();
          log.info("Consumer" + id + " get product from supplier with ID" + tmp.getSupplierId());
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