package ru.nsu.zhdanov.lab_4.model.factory.engine_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartRepository;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class EngineRepository extends SparePartRepository<Engine> {
  public EngineRepository(final int repositorySize) {
    super(SparePartType.ENGINE, repositorySize);
  }
}
