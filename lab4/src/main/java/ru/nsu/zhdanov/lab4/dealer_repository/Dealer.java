package ru.nsu.zhdanov.lab4.dealer_repository;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class Dealer<Car, Repository extends CarSupplier<Car>> implements Runnable {
  CarSupplier<Car> repository;
  int delay;

  Dealer(int delay, Repository repository) {
    this.delay = delay;
    this.repository = repository;
  }

  @Override
  public void run() {
    while (Thread.currentThread().isAlive()) {
      Car car = repository.getCar();
      log.info(car.toString());

      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
