package ru.nsu.zhdanov.lab4.parts_section.body_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BodyRepository implements Runnable, SparePartSupplier<Body> {
  @Setter
  private SparePartSupplier<Body> bodySupplier;
  final private BlockingQueue<Body> repository;

  public BodyRepository(final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      synchronized (repository) {
        Body tmp = bodySupplier.getSparePart();
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
  public Body getSparePart() {
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
