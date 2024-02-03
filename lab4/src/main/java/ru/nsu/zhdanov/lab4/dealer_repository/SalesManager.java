package ru.nsu.zhdanov.lab4.dealer_repository;

import ru.nsu.zhdanov.lab4.car_factory.Car;
import ru.nsu.zhdanov.lab4.car_factory.CarSupplier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SalesManager<> implements Runnable {
  CarSupplier<Car> repository; //todo здесь должен быть CarSupplierFacade.java
  AtomicInteger delay;

  SalesManager(final AtomicInteger delay, CarSupplier<Car> repository) {
    this.delay = delay;
    this.repository = repository;
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      Car car = repository.getCar();
      log.info(car.toString());

      try {
        Thread.sleep(delay.get());
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
