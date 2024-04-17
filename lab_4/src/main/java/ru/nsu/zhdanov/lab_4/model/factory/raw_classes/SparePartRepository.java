package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class SparePartRepository<SparePartT> implements SparePartSupplier<SparePartT>, SparePartConsumer<SparePartT> {
  @Setter
  protected SparePartSupplier supplier;
  final private BlockingQueue<SparePartT> repository;
  protected final SparePartType sparePartType;

  public SparePartRepository(final SparePartType sparePartName,
                             final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
    this.sparePartType = sparePartName;
  }

  public int remainingCapacity() {
    return repository.remainingCapacity();
  }

  @Override
  public SparePartT getSparePart() throws InterruptedException {
    log.info("getSparePart() " + sparePartType);
    return repository.take();
  }

  @Override
  public void acceptSparePart(SparePartT sparePart) throws InterruptedException {
    repository.put(sparePart);
    log.info("acceptSparePart() " + sparePartType);
  }
}
