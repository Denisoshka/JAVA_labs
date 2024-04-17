package ru.nsu.zhdanov.lab_4.model.factory.body_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class Body extends SparePart {

  public Body() {
    super();
  }
@Override
  public SparePartType getSparePatType() {
    return SparePartType.BODY;
  }
}
