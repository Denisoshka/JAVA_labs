package ru.nsu.zhdanov.lab4.car_factory;

public class CarRepositoryController<Car> {
  //  Repository repository;
  final CarRepository<Car, CarRepositoryController<Car>> repository;
  final int dealersQuantity;
  final Thread worker;
  Runnable task = () -> {
    while (Thread.currentThread().isAlive()) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      synchronized (repository) {
        int repSize = repository.getSize();
        int repRemCap = repository.getRemainingCapacity();
//todo make controller request implementation
      }
    }
  };

  public CarRepositoryController(CarRepository<Car, CarRepositoryController<Car>> repository, int dealersQuantity) {
    this.repository = repository;
    this.dealersQuantity = dealersQuantity;
    this.worker = new Thread(task);
  }

  public void perform(){
    worker.start();
  }

  public void shutdown(){
    worker.interrupt();
  }
}
