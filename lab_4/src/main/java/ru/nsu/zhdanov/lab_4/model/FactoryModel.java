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
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartModelMonitorListener;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.MonitorListenerIntroduction;

@Slf4j
public class FactoryModel implements SetDelayInterface {
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

  public void perform() {
    log.trace("perform");
    this.controller.perform();
  }

  public void shutdown() {
    log.trace("shutdown");
    this.factory.shutdown();
    this.controller.shutdown();
  }

  @Override
  public void setDelay(int delay) {
    factory.setDelay(delay);
  }

  public CarFactory getFactory() {
    return factory;
  }
}
