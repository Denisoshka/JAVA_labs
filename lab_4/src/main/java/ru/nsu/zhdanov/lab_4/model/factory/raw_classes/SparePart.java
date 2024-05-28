package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import java.util.UUID;

public abstract class SparePart {
  protected UUID id;

  public SparePart() {
    this.id = UUID.randomUUID();
  }

  @Override
  public String toString() {
    return getSparePatType() + " " + id.toString();
  }

  public abstract SparePartType getSparePatType();

  public UUID getId() {
    return this.id;
  }
}
