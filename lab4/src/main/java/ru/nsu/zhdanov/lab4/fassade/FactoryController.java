package ru.nsu.zhdanov.lab4.fassade;

import ru.nsu.zhdanov.lab4.car_factory.Car;
import ru.nsu.zhdanov.lab4.car_factory.CarFactory;
import ru.nsu.zhdanov.lab4.car_factory.CarRepository;
import ru.nsu.zhdanov.lab4.car_factory.CarRepositoryController;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.AccessoriesRepository;
import ru.nsu.zhdanov.lab4.parts_section.body_section.BodyRepository;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineRepository;

import java.util.concurrent.atomic.AtomicInteger;

public class FactoryController {
  AtomicInteger delay;
  CarFactory factory;
  CarRepositoryController<Car> controller;
  CarRepository<Car, CarRepositoryController<Car>> repository;

  FactoryController(final int workersQuantity, final int repoSize, final int dealersQuantity, final int delay,
                    final BodyRepository bodyRepo, final EngineRepository engineRepo, final AccessoriesRepository accRepo) {
    this.delay = new AtomicInteger(delay);
    this.factory = new CarFactory(workersQuantity, this.delay);
    this.repository = new CarRepository<>(repoSize);
    this.controller = new CarRepositoryController<>(this.repository, dealersQuantity);
    factory.setCarRepo(repository);
    factory.setBodyRep(bodyRepo);
    factory.setAccRep(accRepo);
    factory.setEngRep(engineRepo);
  }
}
