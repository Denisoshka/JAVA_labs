package ru.nsu.zhdanov.lab_4.dealer_repository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.car_factory.Car;
import ru.nsu.zhdanov.lab_4.car_factory.CarSupplier;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarDealerCentre {
  final ArrayList<Thread> managers;
  @Getter
  AtomicInteger delay;
  final protected CarSupplier carRepo;
  Runnable task;

  public CarDealerCentre(final CarSupplier carRepo ,final int managersQuantity, final AtomicInteger delay) {
    this.task = () -> {
      while (Thread.currentThread().isAlive()) {
        try {
          log.info("request car");
          Car car = carRepo.getCar();
          log.info("sell car" + car.toString());
          log.info("cur delay " + delay.get());
          Thread.sleep(delay.get());
        } catch (InterruptedException e) {
          return;
        }
      }
    };

    this.carRepo = carRepo;
    this.delay = delay;
    managers = new ArrayList<>(managersQuantity);
    for(int i = 0; i < managersQuantity; i++){
      managers.add(new Thread(task));
    }
    log.info("init managersQuantity: " + this.managers.size()+ " delay: " + delay);
  }

  public void perform() {
    log.info("perform");
    for (Thread mngr : managers) {
      mngr.start();
    }
  }

  public void shutdown() {
    log.info("shutdown");
    for (Thread mngr : managers) {
      mngr.interrupt();
    }
  }
}
