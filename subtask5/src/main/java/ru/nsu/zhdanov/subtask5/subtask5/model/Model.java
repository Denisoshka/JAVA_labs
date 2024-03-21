package ru.nsu.zhdanov.subtask5.subtask5.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Model {
  private final Consumers consumers;
  private final Suppliers suppliers;

  private final AtomicInteger supplierDelay = new AtomicInteger(1000);

  public Model(int storageCapacity, int consumersQuantity, int suppliersQuantity) {
    Storage<Product> storage = new Storage<>(storageCapacity);
    this.suppliers = new Suppliers(suppliersQuantity, supplierDelay, storage);
    this.consumers = new Consumers(consumersQuantity, storage);
  }

  public AtomicInteger getSupplierDelay() {
    return supplierDelay;
  }

  public void stop() {
    suppliers.stop();
    consumers.stop();
  }
}
