package ru.nsu.zhdanov.lab_4.parts_section.accessories_section;

import ru.nsu.zhdanov.lab_4.parts_section.SparePartRepository;

public class AccessoriesRepository extends SparePartRepository<Accessories> {
  public AccessoriesRepository(final int repositorySize) {
    super(repositorySize);
  }
}
