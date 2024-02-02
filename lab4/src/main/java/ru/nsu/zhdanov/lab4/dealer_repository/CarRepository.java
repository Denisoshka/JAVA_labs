package ru.nsu.zhdanov.lab4.dealer_repository;

import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CarRepository<Car, Controller> implements CarSupplier<Car> {
  final BlockingQueue<Car> repository;
  @Setter
  Controller controller;
  @Setter
  AtomicInteger delay;

  CarRepository(int repSize) {
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
        return repository.take();
      } catch (InterruptedException ignored) {
        return null;
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
