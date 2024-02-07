package ru.nsu.zhdanov.lab4.facade;

import lombok.Getter;
import ru.nsu.zhdanov.lab4.parts_section.SparePart;
import ru.nsu.zhdanov.lab4.parts_section.SparePartProvider;
import ru.nsu.zhdanov.lab4.parts_section.SparePartRepository;

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
    provider.shutdown();
  }
}
