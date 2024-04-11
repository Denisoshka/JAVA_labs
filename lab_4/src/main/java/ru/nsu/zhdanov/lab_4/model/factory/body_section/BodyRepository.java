package ru.nsu.zhdanov.lab_4.model.factory.body_section;

import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartRepository;

public class BodyRepository extends SparePartRepository<Body> {
  public BodyRepository(final int repositorySize) {
    super("BODY", repositorySize);
  }
}
