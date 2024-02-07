package ru.nsu.zhdanov.lab4.facade;

import ru.nsu.zhdanov.lab4.car_factory.Car;
import ru.nsu.zhdanov.lab4.car_factory.CarFactory;
import ru.nsu.zhdanov.lab4.car_factory.CarRepository;
import ru.nsu.zhdanov.lab4.car_factory.CarRepositoryController;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.AccessoriesRepository;
import ru.nsu.zhdanov.lab4.parts_section.body_section.BodyRepository;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineRepository;

import java.util.concurrent.atomic.AtomicInteger;

public class FactoryController {

  AtomicInteger factoryDelay;
  CarFactory factory;
  CarRepositoryController<Car> controller;
  CarRepository<Car, CarRepositoryController<Car>> repository;

  FactoryController(final int workersQuantity, final int repoSize,
                    final int dealersQuantity, final int factoryDelay,
                    final BodyRepository bodyRepo, final EngineRepository engineRepo,
                    final AccessoriesRepository accRepo) {
    this.factoryDelay = new AtomicInteger(factoryDelay);
    this.factory = new CarFactory(workersQuantity, this.factoryDelay);
    this.repository = new CarRepository<>(repoSize);
    this.controller = new CarRepositoryController<>(this.repository, dealersQuantity);
    factory.setCarRepo(repository);
    factory.setBodyRep(bodyRepo);
    factory.setAccRep(accRepo);
    factory.setEngRep(engineRepo);
//    todo make repo in MAP
  }

  public int getFactoryDelay() {
    return factoryDelay.get();
  }

  public void setFactoryDelay(final int delay) {
    factoryDelay.set(delay);
  }

  public void perform(){
    this.controller.perform();
  }

  public void shutdown() {
    this.factory.shutdown();
    this.controller.shutdown();
  }


}
