package ru.nsu.zhdanov.lab4.fassade;

import ru.nsu.zhdanov.lab4.parts_section.accessories_section.Accessories;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.AccessoriesProviders;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.AccessoriesRepository;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineProvider;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineRepository;

public class AccessoriesSectionController extends SparePartSectionController<Accessories> {
  public AccessoriesSectionController(final int provideDelay, final int providersQuantity, final int repoSize) {
    super(provideDelay);
    provider = new AccessoriesProviders(providersQuantity, this.providerDelay);
    repository = new AccessoriesRepository(repoSize);
    repository.setSupplier(provider);
  }
}
