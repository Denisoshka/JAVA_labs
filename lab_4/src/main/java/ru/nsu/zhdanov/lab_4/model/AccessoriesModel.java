package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.SparePartSectionController;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.AccessoriesProviders;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.AccessoriesRepository;

public class AccessoriesModel extends SparePartSectionController<Accessories> {
  public AccessoriesModel(final int provideDelay, final int providersQuantity, final int repoSize) {
    super(provideDelay);
    provider = new AccessoriesProviders(providersQuantity, this.providerDelay);
    repository = new AccessoriesRepository(repoSize);
    provider.setRepository(repository);
  }
}
