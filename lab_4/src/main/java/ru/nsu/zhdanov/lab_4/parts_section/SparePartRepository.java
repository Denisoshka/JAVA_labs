package ru.nsu.zhdanov.lab_4.parts_section;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SparePartRepository<SparePartT> implements SparePartSupplier<SparePartT>, SparePartConsumer<SparePartT> {
  @Setter
  protected SparePartSupplier supplier;
  final private List<SparePartT> repository;
  protected final String sparePartName;
  final Lock lock;
  final Condition notFull;
  final Condition notEmpty;

  public SparePartRepository(final String sparePartName, final int repositorySize) {
    this.lock = new ReentrantLock();
    this.notFull = lock.newCondition();
    this.notEmpty = lock.newCondition();
    this.sparePartName = sparePartName;
    this.repository = new ArrayList<>(repositorySize);
  }

  public int remainingCapacity() {
    try {
      lock.lock();
      return repository.size();

    } finally {
      lock.unlock();
    }

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
