package ru.nsu.zhdanov.lab_4.model.dealer_repository;

import org.slf4j.Logger;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.Car;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarDealerCentre implements CarSellListenerIntroduction {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(CarDealerCentre.class);
  private final ExecutorService managers;
  private final CarSupplier carRepo;
  private final int managersQuantity;
  private final List<CarSellListener> listeners = new ArrayList<>(1);
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
            onCarSold(car);
            Thread.sleep(delay);
          }
        } catch (InterruptedException ignored) {
        } catch (Exception e) {
          log.warn("unexpected exception " + e.getMessage());
        } finally {
          log.trace("interrupted");
        }
      });
    }
  }

  @Override
  public void addCarProduceListener(CarSellListener listener) {
    listeners.add(listener);
  }

  private void onCarSold(Car car) {
    try{
      for (var listener : listeners) {
        listener.produced(car);
      }
    }catch (Exception e){
      log.warn("onCarSold", e);
    }
  }

  public void shutdown() {
    managers.shutdownNow();
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
