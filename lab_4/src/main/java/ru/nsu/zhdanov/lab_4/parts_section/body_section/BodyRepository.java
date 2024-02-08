package ru.nsu.zhdanov.lab_4.parts_section.body_section;

import ru.nsu.zhdanov.lab_4.parts_section.SparePartRepository;

public class BodyRepository extends SparePartRepository<Body> {
  public BodyRepository(final int repositorySize) {
    super(repositorySize);
  }
}
