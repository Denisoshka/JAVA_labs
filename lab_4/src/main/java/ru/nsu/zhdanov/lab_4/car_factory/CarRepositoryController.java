package ru.nsu.zhdanov.lab_4.car_factory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CarRepositoryController<Car> {
  //  Repository repository;
  CarRepository<Car, CarRepositoryController<Car>> repository = null;
  CarsRequest factory;
  int dealersQuantity = 0;
  final Thread worker;
  final Runnable task;

  public CarRepositoryController(CarRepository<Car, CarRepositoryController<Car>> repository, int dealersQuantity) {
    log.info("init CarRepositoryController");
    this.task =  () -> {
      while (Thread.currentThread().isAlive()) {
        synchronized (repository){
          try {
            wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          if (repository.getSize() - repository.getRemainingCapacity() < dealersQuantity) {
            factory.requestCars(repository.getRemainingCapacity());
          }
        }
      }
    };
    this.repository = repository;
    this.dealersQuantity = dealersQuantity;
    this.worker = new Thread(task);
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
