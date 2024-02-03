package ru.nsu.zhdanov.lab4.car_factory;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.car_factory.CarSupplier;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CarRepository<Car, Controller> implements CarSupplier<Car>, CarConsumer<Car> {
  final BlockingQueue<Car> repository;
  @Setter
  Controller controller;
  @Setter
  AtomicInteger delay;

  public CarRepository(int repSize) {
    this.repository = new ArrayBlockingQueue<>(repSize);
  }

  @Override
  public Car getCar() {
    controller.notify();
    synchronized (repository) {
      try {
        while (repository.isEmpty()) {
          wait();
        }
        Car tmp = repository.take();
        repository.notifyAll();
        return tmp;
      } catch (InterruptedException ignored) {
        return null;
      }
    }
  }
  @Override
  public void acceptCar(Car car) {
    synchronized (repository){
      try{
        while(repository.remainingCapacity() == 0){
          wait();
        }
        repository.add(car);
      } catch (InterruptedException ignored) {
      }
    }
  }
  synchronized public int getSize() {
    return repository.size();
  }

  synchronized public int getRemainingCapacity() {
    return repository.remainingCapacity();
  }


}
