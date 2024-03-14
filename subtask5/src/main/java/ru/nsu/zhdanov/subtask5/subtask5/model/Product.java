package ru.nsu.zhdanov.subtask5.subtask5.model;

public class Product {
  private final int supplierId;

  Product(int id) {
    this.supplierId = id;
  }

  public int getSupplierId() {
    return this.supplierId;
  }
}
