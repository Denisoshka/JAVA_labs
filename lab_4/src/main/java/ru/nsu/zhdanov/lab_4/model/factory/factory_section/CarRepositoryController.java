package ru.nsu.zhdanov.lab_4.model.factory.factory_section;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CarRepositoryController implements CatOrderedListener {
  private final CarRepository repository;
  private final CarsRequest factory;
  private final int dealersQuantity;
  private final ExecutorService worker;

  public CarRepositoryController(CarRepository repository, CarFactory factory, int dealersQuantity) {
    this.repository = repository;
    this.factory = factory;
    this.dealersQuantity = dealersQuantity;
    this.worker = Executors.newSingleThreadExecutor();
  }

  public void perform() {
    worker.submit(() -> {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          synchronized (this) {
            this.wait();
            if (repository.occupancy() < dealersQuantity) {
              factory.requestCars(repository.getRemainingCapacity());
              log.trace("request " + repository.getRemainingCapacity());
            }
          }
        }
      } catch (InterruptedException ignored) {
      } catch (Exception e) {
        log.warn("unexpected exception " + e.getMessage());
      } finally {
        log.trace("interrupted");
      }
    });
  }

  public void shutdown() {
    worker.shutdownNow();
  }

  @Override
  public void carOrdered() {
    synchronized (this) {
      this.notifyAll();
    }
  }
}
