package ru.nsu.zhdanov.lab_4.model.factory.factory_section;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.thread_pool.CustomThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarFactory implements CarsRequest {
  final private CustomThreadPool workers;
  final private SparePartSupplier<Body> bodyRepo;
  final private SparePartSupplier<Engine> engRepo;
  final private SparePartSupplier<Accessories> accRepo;
  final private CarConsumer carRepo;
  final private @Getter AtomicInteger delay;
  final private Runnable task;

  public CarFactory(CarConsumer carRepo, SparePartSupplier<Body> bodyRepo,
                    SparePartSupplier<Engine> engRepo,
                    SparePartSupplier<Accessories> accRepo,
                    final int workersQuantity, final AtomicInteger delay) {
    this.delay = delay;
    this.bodyRepo = bodyRepo;
    this.engRepo = engRepo;
    this.accRepo = accRepo;
    this.carRepo = carRepo;

    log.info("make CustomThreadPool");
    this.workers = new CustomThreadPool(workersQuantity);
    this.task = () -> {
      log.info("make new car request");
      Body body;
      Engine engine;
      Accessories acc;
      try {
        body = this.bodyRepo.getSparePart();
        engine = this.engRepo.getSparePart();
        acc = this.accRepo.getSparePart();
        log.debug("make new car");
        Thread.sleep(delay.get());
      } catch (InterruptedException e) {
        return;
      }
      log.info("new Car(body, engine, acc) " + body.toString() + " " + engine.toString() + " " + acc.toString());
      Car car = new Car(body, engine, acc);
      log.info("call carRepo.acceptCar(car)");
      carRepo.acceptCar(car);
    };
    log.info("init CarFactory workersQuantity:" + workersQuantity + " " + this.bodyRepo + " " + this.engRepo + " " + this.accRepo + " delay: " + this.delay);
  }

  public void shutdown() {
    log.info("shutdown");
    this.workers.shutdown();
  }

  @Override
  public void requestCars(int orderSize) {
    log.info("request Cars: " + orderSize);
    for (int i = 0; i < orderSize; ++i) {
      workers.execute(task);
    }
  }
}
