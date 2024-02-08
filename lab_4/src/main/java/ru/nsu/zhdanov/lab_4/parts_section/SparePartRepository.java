package ru.nsu.zhdanov.lab_4.parts_section;

import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SparePartRepository<SparePartT extends SparePart> implements SparePartSupplier<SparePartT>, SparePartConsumer<SparePartT> {
  @Setter
  protected SparePartSupplier<SparePartT> supplier;
  final private BlockingQueue<SparePartT> repository;

  public SparePartRepository(final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  public int remainingCapacity() {
    return repository.remainingCapacity();
  }

  @Override
  public SparePartT getSparePart() {
    synchronized (repository) {
      try {
        while (repository.isEmpty()) {
          wait();
        }
        return repository.take();
      } catch (InterruptedException ignored) {
        return null;
      } finally {
        repository.notifyAll();
      }
    }
  }

  @Override
  public void acceptCar(SparePartT car) {
    synchronized (repository) {
      try {
        while (repository.remainingCapacity() == 0) {
          wait();
        }
        repository.add(car);
        repository.notifyAll();
      } catch (InterruptedException ignored) {
      }
    }
  }
}
