package ru.nsu.zhdanov.lab_4.facade;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.car_factory.*;
import ru.nsu.zhdanov.lab_4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab_4.parts_section.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab_4.parts_section.engine_section.Engine;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FactoryController {
  private final AtomicInteger factoryDelay;
  private final CarFactory factory;
  private final CarRepositoryController controller;
  private final CarRepository repository;

  FactoryController(final int workersQuantity, final int repoSize,
                    final int dealersQuantity, final int factoryDelay,
                    final SparePartSupplier<Body> bodyRepo,
                    final SparePartSupplier<Engine> engineRepo,
                    final SparePartSupplier<Accessories> accRepo) {
    log.info("init FactoryController");
    this.factoryDelay = new AtomicInteger(factoryDelay);
    this.repository = new CarRepository(repoSize);
    this.factory = new CarFactory(this.repository, bodyRepo, engineRepo, accRepo, workersQuantity, this.factoryDelay);
    this.controller = new CarRepositoryController(this.repository, this.factory, dealersQuantity);
    this.repository.setController( this.controller);
  }

  public CarSupplier getCarSupplier() {
    return repository;
  }

  public int getRepositoryOccupancy() {
    return repository.occupancy();
  }

  public int getFactoryDelay() {
    return factoryDelay.get();
  }

  public void setFactoryDelay(final int delay) {
    log.info("set new FactoryDelay: " + delay);
    factoryDelay.set(delay);
  }

  public void perform() {
    log.info("perform");
    this.controller.perform();
  }

  public void shutdown() {
    log.info("shut down factory");
    this.factory.shutdown();
    this.controller.shutdown();
  }


}
