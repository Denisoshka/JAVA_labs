package ru.nsu.zhdanov.lab_4.model;

import ru.nsu.zhdanov.lab_4.model.dealer_repository.CarDealerCentre;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarSupplier;

public class DealerModel {
  private final CarDealerCentre dealer;

  public DealerModel(final CarSupplier carRepo, final int managersQuantity, final int delay) {
    this.dealer = new CarDealerCentre(carRepo, managersQuantity, delay);
  }

  public void setDelay(final int delay) {
    dealer.setDelay(delay);
  }

  public void perform() {
    dealer.perform();
  }

  public void shutdown() {
    dealer.shutdown();
  }

  public CarDealerCentre getDealer() {
    return dealer;
  }
}
