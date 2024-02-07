package ru.nsu.zhdanov.lab4.facade;

import ru.nsu.zhdanov.lab4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab4.parts_section.body_section.BodyProvider;
import ru.nsu.zhdanov.lab4.parts_section.body_section.BodyRepository;

public class BodySectionController extends SparePartSectionController<Body>{
  public BodySectionController(int provideDelay, int repoSize) {
    super(provideDelay);
    repository = new BodyRepository(repoSize);
    provider = new BodyProvider(this.providerDelay);
    provider.setRepository(repository);
  }
}
