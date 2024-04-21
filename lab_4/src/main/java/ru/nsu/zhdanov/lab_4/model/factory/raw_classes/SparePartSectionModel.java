package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SetDelayInterface;

public class SparePartSectionModel<SparePartT extends SparePart> implements SetDelayInterface {
  private SparePartProvider<SparePartT> provider;
  private SparePartRepository<SparePartT> repository;

  public SparePartSectionModel(SparePartProvider<SparePartT> provider, SparePartRepository<SparePartT> repository) {
    this.repository = repository;
    this.provider = provider;
    this.provider.setRepository(repository);
  }

  @Override
  public void setDelay(int delay) {
    provider.setDelay(delay);
  }

  public void perform() {
    provider.perform();
  }

  public void shutdownNow() {
    provider.shutdownNow();
  }

  public SparePartProvider<SparePartT> getProvider() {
    return provider;
  }

  public SparePartRepository<SparePartT> getRepository() {
    return repository;
  }
}
