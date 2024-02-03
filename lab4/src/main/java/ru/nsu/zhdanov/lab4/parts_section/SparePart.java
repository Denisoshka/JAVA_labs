package ru.nsu.zhdanov.lab4.parts_section;

import lombok.Getter;

import java.util.UUID;

public class SparePart {
  @Getter
  protected static String partName;
  @Getter
  protected UUID id;

  public SparePart() {
    this.id = UUID.randomUUID();
  }
  @Override
  public String toString(){
    return partName + " "  + id.toString();
  }
}
