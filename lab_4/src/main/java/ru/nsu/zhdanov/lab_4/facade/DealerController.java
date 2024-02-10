package ru.nsu.zhdanov.lab_4.facade;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.car_factory.Car;
import ru.nsu.zhdanov.lab_4.car_factory.CarSupplier;
import ru.nsu.zhdanov.lab_4.dealer_repository.CarDealerCentre;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DealerController {
  final AtomicInteger delay;
  CarDealerCentre dealer;

  public DealerController(final CarSupplier carRepo, final int managersQuantity, final int delay) {
//    todo fix this
    this.delay = new AtomicInteger(delay);
    this.dealer = new CarDealerCentre(carRepo, managersQuantity, this.delay);
  }

  public void setDelay(final int delay) {
    log.info("set delay " + delay);
    this.delay.set(delay);
  }

  public void perform() {
    log.info("perform");
    dealer.perform();
  }

  public void shutdown() {
    log.info("shutdown");
    dealer.shutdown();
  }
}
