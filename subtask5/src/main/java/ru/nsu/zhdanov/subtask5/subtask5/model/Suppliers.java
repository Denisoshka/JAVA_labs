package ru.nsu.zhdanov.subtask5.subtask5.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Suppliers {
  private final ExecutorService executors;
  private final AtomicInteger delay;

  public Suppliers(int workersQuantity, AtomicInteger delayBind, Consumer<Product> consumer) {
    this.delay = delayBind;
    executors = Executors.newFixedThreadPool(workersQuantity);
    for (int i = 0; i < workersQuantity; ++i) {
      executors.submit(new ProductSupplier(consumer));
    }
  }

  public void stop() {
    executors.shutdownNow();
  }

  private class ProductSupplier implements Runnable {
    private static int ID = 0;
    private final int id;
    private final Consumer<Product> consumer;

    ProductSupplier(Consumer<Product> consumer) {
      id = ID++;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          consumer.accept(new Product(id));
          Thread.sleep(delay.get());
        } catch (InterruptedException e) {
          return;
        }
      }
    }
  }
}
