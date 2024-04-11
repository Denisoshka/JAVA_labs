package ru.nsu.zhdanov.lab_4.model.factory.engine_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;

public class Engine extends SparePart {
  public Engine() {
    super();
  }

  @Override
  public String getSparePartName() {
    return "ENGINE";
  }
}
