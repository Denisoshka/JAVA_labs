package ru.nsu.zhdanov.lab_4.model.factory.body_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;

public class Body extends SparePart {

  public Body() {
    super();
  }
@Override
  public String  getSparePartName() {
    return "BODY";
  }
}
