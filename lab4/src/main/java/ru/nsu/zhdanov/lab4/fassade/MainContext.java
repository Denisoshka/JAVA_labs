package ru.nsu.zhdanov.lab4.fassade;

import ru.nsu.zhdanov.lab4.Main;
import ru.nsu.zhdanov.lab4.car_factory.CarFactory;
import ru.nsu.zhdanov.lab4.car_factory.CarRepository;
import ru.nsu.zhdanov.lab4.car_factory.CarRepositoryController;
import ru.nsu.zhdanov.lab4.dealer_repository.CarDealerCentre;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.AccessoriesRepository;
import ru.nsu.zhdanov.lab4.parts_section.body_section.BodyProvider;
import ru.nsu.zhdanov.lab4.parts_section.body_section.BodyRepository;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineProvider;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.EngineRepository;
import ru.nsu.zhdanov.lab4.parts_section.exceptions.ClassLoaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class MainContext {
  private final Properties workersProperties;

  public MainContext(final String properties) throws IOException {
    ClassLoader classLoader;
    try {
      classLoader = Main.class.getClassLoader();
      if (classLoader == null) {
        throw new ClassLoaderException();
      }
    } catch (SecurityException e) {
      throw new ClassLoaderException();
    }

    try (InputStream in = classLoader.getResourceAsStream(properties)) {
      (this.workersProperties = new Properties()).load(in);
    } catch (Exception e) {
//      todo
      throw e;
    }

//    this.bodyRepo = bodyRepo;
//    this.bodyProvider = bodyProvider;
//    this.carRepo = carRepo;
//    this.carFactory = new CarFactory;
//    this.carRepoController = carRepoController;
//    this.carDealer = carDealer;
//    this.accProvider = accProvider;
//    this.accRepo = accRepo;
//    this.engineProvider = engineProvider;
//    this.engineRepo = engineRepo;
  }



}
