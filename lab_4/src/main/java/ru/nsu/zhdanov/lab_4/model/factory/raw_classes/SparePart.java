package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Getter;

import java.util.UUID;

public abstract class SparePart {
  @Getter
  protected UUID id;

  public SparePart() {
    this.id = UUID.randomUUID();
  }

  @Override
  public String toString() {
    return getSparePartName() + " " + id.toString();
  }

  public abstract String getSparePartName();
}
