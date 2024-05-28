package ru.nsu.zhdanov.lab_4.model.factory.interfaces;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;

public interface SparePartFactoryInterface {
  SparePart make(String title);
}
