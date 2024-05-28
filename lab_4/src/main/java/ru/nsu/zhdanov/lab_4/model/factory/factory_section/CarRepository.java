package ru.nsu.zhdanov.lab_4.model.factory.factory_section;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CarRepository implements CarSupplier, CarConsumer {
  final BlockingQueue<Car> repository;
  private CarRepositoryController controller;

  public CarRepository(int repSize) {
    this.repository = new ArrayBlockingQueue<>(repSize);
  }

  @Override
  public Car getCar() throws InterruptedException {
    controller.carOrdered();
    return repository.take();
  }

  @Override
  public void acceptCar(Car car) {
    try {
      repository.put(car);
    } catch (InterruptedException ignored) {
    }
  }

  public int getRemainingCapacity() {
    return repository.remainingCapacity();
  }

  public int occupancy() {
    return repository.size();
  }

  public void setController(CarRepositoryController controller) {
    this.controller = controller;
  }
}
