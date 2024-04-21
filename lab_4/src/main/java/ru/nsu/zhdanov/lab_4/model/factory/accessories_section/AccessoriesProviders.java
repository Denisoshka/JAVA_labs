package ru.nsu.zhdanov.lab_4.model.factory.accessories_section;

import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class AccessoriesProviders extends SparePartProvider<Accessories> {
  public AccessoriesProviders(final SparePartFactoryInterface factory, final int providersQuantity, final int delay) {
    super(factory, SparePartType.ACCESSORIES, providersQuantity, delay);
  }
}
