package ru.nsu.zhdanov.lab4.parts_section.accessories_section;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EngineRepository implements Runnable, SparePartSupplier<ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine> {
  @Setter
  private SparePartSupplier<ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine> bodySupplier;
  final private BlockingQueue<ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine> repository;

  public EngineRepository(final int repositorySize) {
    this.repository = new ArrayBlockingQueue<>(repositorySize);
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      synchronized (repository) {
        ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine tmp = bodySupplier.getSparePart();
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
  public Engine getSparePart() {
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
