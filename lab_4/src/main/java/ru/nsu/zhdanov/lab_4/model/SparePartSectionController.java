package ru.nsu.zhdanov.lab_4.model;

import lombok.Getter;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePart;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartProvider;
import ru.nsu.zhdanov.lab_4.model.factory.raw_classes.SparePartRepository;

import java.util.concurrent.atomic.AtomicInteger;

public class SparePartSectionController<SparePartT extends SparePart> {
  protected final AtomicInteger providerDelay;
  protected @Getter SparePartProvider<SparePartT> provider;
  protected @Getter SparePartRepository<SparePartT> repository;

  public SparePartSectionController(final int provideDelay) {
    this.providerDelay = new AtomicInteger(provideDelay);
  }

  public void setProviderDelay(final int providerDelay) {
    this.providerDelay.set(providerDelay);
  }

  public void getProviderDelay() {
    this.providerDelay.get();
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
}
