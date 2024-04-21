package ru.nsu.zhdanov.lab_4.model.dealer_repository;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.Car;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarSupplier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CarDealerCentre {
  private final ExecutorService managers;
  private final CarSupplier carRepo;
  private final int managersQuantity;
  private volatile int delay;

  public CarDealerCentre(final CarSupplier carRepo, final int managersQuantity, final int delay) {
    this.managersQuantity = managersQuantity;
    this.carRepo = carRepo;
    this.delay = delay;
    this.managers = Executors.newFixedThreadPool(managersQuantity);
  }

  public void perform() {
    for (int i = 0; i < managersQuantity; i++) {
      managers.submit(() -> {
        try {
          log.trace("start");
          while (!Thread.currentThread().isInterrupted()) {
            Car car = carRepo.getCar();
//          todo make here interface for loging
            log.info("sell car" + car.toString());
            Thread.sleep(delay);
          }
        } catch (InterruptedException ignored) {
        } catch (Exception e){
          log.warn("unexpected exception " + e.getMessage());
        }finally {
          log.trace("interrupted");
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
