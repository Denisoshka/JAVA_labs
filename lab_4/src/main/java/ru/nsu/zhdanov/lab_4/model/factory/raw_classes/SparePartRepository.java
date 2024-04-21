package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SparePartRepository<SparePartT> implements SparePartSupplier<SparePartT>, SparePartConsumer<SparePartT> {
  private final BlockingQueue<SparePartT> repository;

  public SparePartRepository(final SparePartType sparePartName, final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  @Override
  public SparePartT getSparePart() throws InterruptedException {
    return repository.take();
  }

  @Override
  public void acceptSparePart(SparePartT sparePart) throws InterruptedException {
    repository.put(sparePart);
  }
}
