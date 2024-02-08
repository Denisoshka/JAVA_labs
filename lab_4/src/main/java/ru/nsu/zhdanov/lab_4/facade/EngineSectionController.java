package ru.nsu.zhdanov.lab_4.facade;

import ru.nsu.zhdanov.lab_4.parts_section.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.parts_section.engine_section.EngineProvider;
import ru.nsu.zhdanov.lab_4.parts_section.engine_section.EngineRepository;

public class EngineSectionController extends SparePartSectionController<Engine> {
  public EngineSectionController(int provideDelay, int repoSize) {
    super(provideDelay);
    provider = new EngineProvider(this.providerDelay);
    repository = new EngineRepository(repoSize);
    provider.setRepository(repository);
  }
}
