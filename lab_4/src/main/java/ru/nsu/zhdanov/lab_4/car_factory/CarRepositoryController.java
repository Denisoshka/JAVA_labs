package ru.nsu.zhdanov.lab_4.car_factory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CarRepositoryController {
  private final CarRepository repository;
  private final CarsRequest factory;
  private final int dealersQuantity;
  private final Thread worker;
  private final Runnable task;

  public CarRepositoryController(CarRepository repository, CarFactory factory, int dealersQuantity) {
    this.repository = repository;
    this.factory = factory;
    this.dealersQuantity = dealersQuantity;
    this.task = () -> {
      while (Thread.currentThread().isAlive()) {
        synchronized (this) {
          try {
            this.wait();
          } catch (InterruptedException e) {
            return;
          }
          log.debug("controller is awake make request");
          if (repository.occupancy() < dealersQuantity) {
            log.debug("request cars: " + repository.getRemainingCapacity());
            factory.requestCars(repository.getRemainingCapacity());
          }
        }
      }
    };
    this.worker = new Thread(task);
    log.info("init CarRepositoryController");
  }

  public void perform() {
    log.info("perform");
    worker.start();
  }

  public void shutdown() {
    log.info("shutdown");
    worker.interrupt();
  }
}
