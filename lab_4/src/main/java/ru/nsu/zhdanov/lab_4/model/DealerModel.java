package ru.nsu.zhdanov.lab_4.model;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarSupplier;
import ru.nsu.zhdanov.lab_4.model.dealer_repository.CarDealerCentre;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DealerModel {
  final AtomicInteger delay;
  CarDealerCentre dealer;

  public DealerModel(final CarSupplier carRepo, final int managersQuantity, final int delay) {
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
