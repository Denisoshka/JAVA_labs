package ru.nsu.zhdanov.lab_4.car_factory;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarRepository implements CarSupplier, CarConsumer {
  final BlockingQueue<Car> repository;
  private @Setter CarRepositoryController controller;
  @Setter
  AtomicInteger delay;

  public CarRepository(int repSize) {
    this.repository = new ArrayBlockingQueue<>(repSize);
  }

  @Override
  public Car getCar() {
    Car car = null;
    try {
      synchronized (controller) {
        controller.notify();
      }
      car = repository.take();
      log.debug("getCar()");
    } catch (InterruptedException ignored) {
    }
    return car;
  }

  @Override
  public void acceptCar(Car car) {
    try {
      repository.put(car);
      log.debug("acceptCar()");
    } catch (InterruptedException ignored) {
    }
  }

  public int getRemainingCapacity() {
    return repository.remainingCapacity();
  }

  public int occupancy() {
    return repository.size();
  }
}
