package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SetDelayInterface;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartRepository;

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

  public int getProviderDelay() {
    return provider.getDelay();
  }

  public int getProductsQuantity() {
    return repository.remainingCapacity();
  }

  public void perform() {
    provider.perform();
  }

  public void shutdown() {
    provider.shutdownNow();
  }

  public SparePartProvider<SparePartT> getProvider() {
    return provider;
  }

  public SparePartRepository<SparePartT> getRepository() {
    return repository;
  }
}
