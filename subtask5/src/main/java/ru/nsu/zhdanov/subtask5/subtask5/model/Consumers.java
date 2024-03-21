package ru.nsu.zhdanov.subtask5.subtask5.model;

import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Consumers {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Suppliers.class);
  private final ExecutorService executors;

  public Consumers(int workersQuantity, Supplier<Product> supplier) {
    executors = Executors.newFixedThreadPool(workersQuantity);
    for (int i = 0; i < workersQuantity; ++i) {
      executors.submit(new ProductConsumer(supplier));
    }
  }

  public void stop() {
    executors.shutdownNow();
  }

  private static class ProductConsumer implements Runnable {
    private static int ID = 0;
    private final int id;
    private final Supplier<Product> supplier;

    ProductConsumer(Supplier<Product> supplier) {
      id = ID++;
      this.supplier = supplier;
    }

    @Override
    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        Product tmp = supplier.get();
        log.info("Consumer" + id + " get product from supplier with ID" + tmp.getSupplierId());
      }
    }
  }
}