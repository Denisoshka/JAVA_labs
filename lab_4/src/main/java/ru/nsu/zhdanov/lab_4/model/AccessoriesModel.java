package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.AccessoriesProviders;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.AccessoriesRepository;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartSectionModel;

public class AccessoriesModel extends SparePartSectionModel<Accessories> {
  public AccessoriesModel(final SparePartFactoryInterface factory, final int providerDelay, final int providersQuantity, final int repoSize) {
    super(new AccessoriesProviders(factory, providersQuantity, providerDelay), new AccessoriesRepository(repoSize));
  }
}
