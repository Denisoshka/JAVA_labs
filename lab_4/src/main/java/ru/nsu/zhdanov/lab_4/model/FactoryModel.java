package ru.nsu.zhdanov.lab_4.model;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarFactory;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarRepository;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarRepositoryController;
import ru.nsu.zhdanov.lab_4.model.factory.factory_section.CarSupplier;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SetDelayInterface;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FactoryModel implements SetDelayInterface {
  private final AtomicInteger factoryDelay;
  private final CarFactory factory;
  private final CarRepositoryController controller;
  private final CarRepository repository;

  public FactoryModel(
          final int workersQuantity, final int repoSize,
          final int dealersQuantity, final int factoryDelay,
          final SparePartSupplier<Body> bodyRepo,
          final SparePartSupplier<Engine> engineRepo,
          final SparePartSupplier<Accessories> accRepo
  ) {
    log.info("init FactoryController");
    this.factoryDelay = new AtomicInteger(factoryDelay);
    this.repository = new CarRepository(repoSize);
    this.factory = new CarFactory(this.repository, bodyRepo, engineRepo, accRepo, workersQuantity, factoryDelay);
    this.controller = new CarRepositoryController(this.repository, this.factory, dealersQuantity);
    this.repository.setController(this.controller);
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

  public void perform() {
   log.debug("perform");
    this.controller.perform();
  }

  public void shutdown() {
    log.debug("shutdown factory");
    this.factory.shutdown();
    this.controller.shutdown();
  }

  @Override
  public void setDelay(int delay) {
    factoryDelay.set(delay);
  }
}
