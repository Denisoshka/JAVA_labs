package ru.nsu.zhdanov.lab_4.parts_section;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class SparePartRepository<SparePartT> implements SparePartSupplier<SparePartT>, SparePartConsumer<SparePartT> {
  @Setter
  protected SparePartSupplier supplier;
  final private BlockingQueue<SparePartT> repository;
  protected final String sparePartName;

  public SparePartRepository(final String sparePartName, final int repositorySize) {
    this.sparePartName = sparePartName;
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  public int remainingCapacity() {
    return repository.remainingCapacity();
  }

  @Override
  public SparePartT getSparePart() {
    SparePartT part = null;
    try {
      log.info("getSparePart() " + sparePartName);
      part = repository.take();
    } catch (InterruptedException ignored) {
    }
    return part;
  }

  @Override
  public void acceptSparePart(SparePartT sparePart) {
    try {
      repository.put(sparePart);
      log.info("acceptSparePart() " + sparePartName);
    } catch (InterruptedException ignored) {
    }
  }
}
