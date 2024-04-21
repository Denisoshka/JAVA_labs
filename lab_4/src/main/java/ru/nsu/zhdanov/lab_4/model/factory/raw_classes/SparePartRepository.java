package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import org.slf4j.Logger;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartModelMonitorListener;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SparePartRepository<SparePartT> implements SparePartSupplier<SparePartT>, SparePartConsumer<SparePartT> {
  protected SparePartSupplier supplier;
  private final BlockingQueue<SparePartT> repository;
  private final List<SparePartModelMonitorListener> listeners;

  public SparePartRepository(
          final SparePartType sparePartName,
          final int repositorySize
  ) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
    this.listeners = new ArrayList<>();
  }

  public int remainingCapacity() {
    return repository.remainingCapacity();
  }

  @Override
  public SparePartT getSparePart() throws InterruptedException {
    return repository.take();
  }

  @Override
  public void acceptSparePart(SparePartT sparePart) throws InterruptedException {
    repository.put(sparePart);
  }

  public int getOccupancy() {
    return repository.size();
  }

  public void setSupplier(SparePartSupplier supplier) {
    this.supplier = supplier;
  }
}
