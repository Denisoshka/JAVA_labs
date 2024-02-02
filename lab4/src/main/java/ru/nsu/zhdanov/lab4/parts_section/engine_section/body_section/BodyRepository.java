package ru.nsu.zhdanov.lab4.parts_section.engine_section.body_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.PartSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BodyRepository implements Runnable, BodySupplier {
  @Setter
  private PartSupplier<Body> partSupplier;
  final private BlockingQueue<Body> repository;

  public BodyRepository(final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      synchronized (repository) {
        Body tmp = partSupplier.getSparePart();
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
  public Body getBody() {
    synchronized (repository) {
      try {
        return repository.take();
      } catch (InterruptedException ignored) {
        return null;
      }
    }
  }
}
