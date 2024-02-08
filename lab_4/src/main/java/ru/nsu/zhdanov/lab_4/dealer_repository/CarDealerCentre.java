package ru.nsu.zhdanov.lab_4.dealer_repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.car_factory.CarSupplier;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarDealerCentre<Car> {
  final ArrayList<Thread> managers;
   @Getter AtomicInteger delay;
  protected @Setter CarSupplier<Car> carRepo;
  Runnable task = () -> {
    while (Thread.currentThread().isAlive()) {
      try {
        Car car = carRepo.getCar();
        log.info(car.toString());
        Thread.sleep(delay.get());
      } catch (InterruptedException e) {
        return;
      }
    }
  };

  public CarDealerCentre(final int managersQuantity, final AtomicInteger delay) {
    this.delay = delay;
    managers = new ArrayList<>(managersQuantity);

    for (ListIterator<Thread> x = managers.listIterator(); x.hasNext(); x.next()) {
      x.set(new Thread(task));
    }
  }

  public void perform() {
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
