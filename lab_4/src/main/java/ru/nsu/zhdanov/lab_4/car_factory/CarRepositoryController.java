package ru.nsu.zhdanov.lab_4.car_factory;

public class CarRepositoryController<Car> {
  //  Repository repository;
  CarRepository<Car, CarRepositoryController<Car>> repository = null;
  CarsRequest factory;
  int dealersQuantity = 0;
  final Thread worker;
  Runnable task = () -> {
    while (Thread.currentThread().isAlive()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      synchronized (repository) {
        if (repository.getSize() - repository.getRemainingCapacity() < dealersQuantity) {
          factory.requestCars(repository.getRemainingCapacity());
        }
      }
    }
  };

  public CarRepositoryController(CarRepository<Car, CarRepositoryController<Car>> repository, int dealersQuantity) {
    this.repository = repository;
    this.dealersQuantity = dealersQuantity;
    this.worker = new Thread(task);
  }

  public void perform() {
    worker.start();
  }

  public void shutdown() {
    worker.interrupt();
  }
}
