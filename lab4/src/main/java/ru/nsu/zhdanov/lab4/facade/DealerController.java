package ru.nsu.zhdanov.lab4.facade;

import ru.nsu.zhdanov.lab4.car_factory.Car;
import ru.nsu.zhdanov.lab4.dealer_repository.CarDealerCentre;

import java.util.concurrent.atomic.AtomicInteger;

public class DealerController {
  final AtomicInteger delay;
  CarDealerCentre<Car> dealer;

  public DealerController(final int managersQuantity, final int delay) {
    this.delay = new AtomicInteger(delay);
    this.dealer = new CarDealerCentre<>(managersQuantity, this.delay);
  }

  public void perform() {
    dealer.perform();
  }

  public void shutdown() {
    dealer.shutdown();
  }
}
