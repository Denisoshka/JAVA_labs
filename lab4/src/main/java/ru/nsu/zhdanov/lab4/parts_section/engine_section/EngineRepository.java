package ru.nsu.zhdanov.lab4.parts_section.engine_section;

import ru.nsu.zhdanov.lab4.parts_section.SparePartRepository;

public class EngineRepository extends SparePartRepository<Engine> {
  public EngineRepository(final int repositorySize) {
    super(repositorySize);
  }
}
