package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.SparePartSectionController;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.BodyProvider;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.BodyRepository;

public class BodyModel extends SparePartSectionController<Body> {
  public BodyModel(int provideDelay, int repoSize) {
    super(provideDelay);
    repository = new BodyRepository(repoSize);
    provider = new BodyProvider(this.providerDelay);
    provider.setRepository(repository);
  }
}
