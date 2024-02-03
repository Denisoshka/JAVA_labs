package ru.nsu.zhdanov.lab4.dealer_repository;

import lombok.Getter;
import ru.nsu.zhdanov.lab4.car_factory.Car;
import ru.nsu.zhdanov.lab4.car_factory.CarSupplier;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CarDealerCentre {
  final ArrayList<Thread> managers;
  final @Getter AtomicInteger delay;

  public CarDealerCentre(CarSupplier<Car> carRepo, final int managersQuantity, int delay) {
    this.delay = new AtomicInteger(delay);
    managers = new ArrayList<>(managersQuantity);

    for (Thread x : managers) {
      x = new Thread(new SalesManager(this.delay, carRepo));
    }
  }

  public void start() {
    for (Thread mngr : managers) {
      mngr.start();
    }
  }

  public void shutdown() {
    for (Thread mngr : managers) {
      mngr.interrupt();
    }
  }
}
