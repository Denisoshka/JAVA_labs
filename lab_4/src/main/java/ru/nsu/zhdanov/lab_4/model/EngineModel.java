package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.EngineProvider;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.EngineRepository;

public class EngineModel extends SparePartSectionController<Engine> {
  public EngineModel(int provideDelay, int repoSize) {
    super(provideDelay);
    provider = new EngineProvider(this.providerDelay);
    repository = new EngineRepository(repoSize);
    provider.setRepository(repository);
  }
}
