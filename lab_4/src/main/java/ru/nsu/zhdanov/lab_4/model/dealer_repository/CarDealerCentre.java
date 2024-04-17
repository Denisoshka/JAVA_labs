package ru.nsu.zhdanov.lab_4.model.dealer_repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.Car;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarSupplier;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarDealerCentre {
  private final ExecutorService managers;

  private volatile int delay;
  final protected CarSupplier carRepo;
  private final int managersQuantity;

  public CarDealerCentre(final CarSupplier carRepo, final int managersQuantity, final int delay) {
    this.managersQuantity = managersQuantity;
    this.carRepo = carRepo;
    this.delay = delay;
    this.managers = Executors.newFixedThreadPool(managersQuantity);
  }

  public void perform() {
    for (int i = 0; i < managersQuantity; i++) {
      managers.submit(() -> {
        while (Thread.currentThread().isAlive()) {
          try {
            log.info("request car");
            Car car = carRepo.getCar();
            log.info("sell car" + car.toString());
            log.info("cur delay " + delay);
            Thread.sleep(delay);
          } catch (InterruptedException e) {
            return;
          }
        }
      });
    }
  }

  public void shutdown() {
    managers.shutdownNow();
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
