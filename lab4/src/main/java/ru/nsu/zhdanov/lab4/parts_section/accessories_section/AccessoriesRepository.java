package ru.nsu.zhdanov.lab4.parts_section.accessories_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartRepository;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AccessoriesRepository extends SparePartRepository<Accessories> {
  public AccessoriesRepository(final int repositorySize) {
    super(repositorySize);
  }
}
