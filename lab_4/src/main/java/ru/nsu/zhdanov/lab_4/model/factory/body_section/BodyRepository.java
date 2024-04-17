package ru.nsu.zhdanov.lab_4.model.factory.body_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartRepository;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartType;

public class BodyRepository extends SparePartRepository<Body> {
  public BodyRepository(final int repositorySize) {
    super(SparePartType.BODY, repositorySize);
  }
}
