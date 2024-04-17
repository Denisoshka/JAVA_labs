package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.EngineProvider;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.EngineRepository;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;

public class EngineModel extends SparePartSectionModel<Engine> {
  public EngineModel(final SparePartFactoryInterface factory, int providerDelay, int repoSize) {
    super(new EngineProvider(factory, providerDelay), new EngineRepository(repoSize));
  }
}
