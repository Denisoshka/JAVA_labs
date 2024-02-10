package ru.nsu.zhdanov.lab_4.parts_section;

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
