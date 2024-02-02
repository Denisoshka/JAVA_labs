package ru.nsu.zhdanov.lab4.dealer_repository;

public class CarRepositoryController<Car> implements Runnable {
  //  Repository repository;
  final CarRepository<Car, CarRepositoryController<Car>> repository;
  final int dealersQuantity;

  public CarRepositoryController(CarRepository<Car, CarRepositoryController<Car>> repository, int dealersQuantity) {
    this.repository = repository;
    this.dealersQuantity = dealersQuantity;
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      synchronized (repository) {
        int repSize = repository.getSize();
        int repRemCap = repository.getRemainingCapacity();
//todo
      }
    }
  }
}
