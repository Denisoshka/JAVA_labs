package ru.nsu.zhdanov.lab4.parts_section;

import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SparePartRepository<SparePartT extends SparePart> implements SparePartSupplier<SparePartT> {
  @Setter
  protected SparePartSupplier<SparePartT> supplier;
  final private BlockingQueue<SparePartT> repository;
  protected Thread worker;
  protected Runnable task = () -> {
    while (Thread.currentThread().isAlive()) {
      synchronized (repository) {
        SparePartT tmp = supplier.getSparePart();
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
  };

  public SparePartRepository(final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
    worker = new Thread(task);
  }

  public void perform() {
    worker.start();
  }

  public void shutdown(){
    worker.interrupt();
  }

  public int remainingCapacity(){
    return repository.remainingCapacity();
  }

  public void run() {
    while (Thread.currentThread().isAlive()) {
      synchronized (repository) {
        SparePartT tmp = supplier.getSparePart();
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
        while (repository.isEmpty()) {
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
