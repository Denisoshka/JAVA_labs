package ru.nsu.zhdanov.lab_4.model.factory.accessories_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartRepository;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class AccessoriesRepository extends SparePartRepository<Accessories> {
  public AccessoriesRepository(final int repositorySize) {
    super(SparePartType.ACCESSORIES, repositorySize);
  }
}
