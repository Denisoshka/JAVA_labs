package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
  public SparePartT getSparePart() throws InterruptedException {
    log.info("getSparePart() " + sparePartName);
    return repository.take();
  }

  @Override
  public void acceptSparePart(SparePartT sparePart) throws InterruptedException {
    repository.put(sparePart);
    log.info("acceptSparePart() " + sparePartName);
  }
}
