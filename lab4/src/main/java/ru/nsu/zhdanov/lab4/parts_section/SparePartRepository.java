package ru.nsu.zhdanov.lab4.parts_section;

import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SparePartRepository<SparePartT extends SparePart> implements Runnable, SparePartSupplier<SparePartT> {
  @Setter
  protected SparePartSupplier<SparePartT> Supplier;
  final private BlockingQueue<SparePartT> repository;

  public SparePartRepository(final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      synchronized (repository) {
        SparePartT tmp = Supplier.getSparePart();
        try {
          repository.put(tmp);
        } catch (InterruptedException e) {
          return;
        }
        try {
          wait();
        } catch (InterruptedException e) {
          return;
        }
      }
    }
  }

  @Override
  public SparePartT getSparePart() {
    synchronized (repository) {
      try {
        while(repository.isEmpty()){
          wait();
        }
        repository.notifyAll();
        return repository.take();
      } catch (InterruptedException ignored) {
        return null;
      }
    }
  }
}
