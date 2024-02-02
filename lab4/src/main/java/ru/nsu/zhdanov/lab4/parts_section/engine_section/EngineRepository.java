package ru.nsu.zhdanov.lab4.parts_section.engine_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartRepository;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EngineRepository extends SparePartRepository<Engine> {
  public EngineRepository(final int repositorySize) {
    super(repositorySize);
  }
}
