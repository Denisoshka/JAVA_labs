package ru.nsu.zhdanov.lab_4.model.factory.accessories_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class Accessories extends SparePart {
  public Accessories() {
    super();
  }

  @Override
  public SparePartType getSparePatType() {
    return SparePartType.ACCESSORIES;
  }
}
