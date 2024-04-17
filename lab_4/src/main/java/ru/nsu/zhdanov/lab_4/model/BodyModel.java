package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.BodyProvider;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.BodyRepository;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;

public class BodyModel extends SparePartSectionModel<Body> {
  public BodyModel(final SparePartFactoryInterface factory, int provideDelay, int repoSize) {
    super(new BodyProvider(factory, provideDelay), new BodyRepository(repoSize));
  }
}
