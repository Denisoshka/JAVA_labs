package ru.nsu.zhdanov.lab_4.model.factory.factory_section;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.thread_pool.CustomFixedThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarFactory implements CarsRequest {
  private final ExecutorService workers;
  private final SparePartSupplier<Body> bodyRepo;
  private final SparePartSupplier<Engine> engRepo;
  private final SparePartSupplier<Accessories> accRepo;
  private final CarConsumer carRepo;
  private volatile int delay;

  public CarFactory(CarConsumer carRepo, SparePartSupplier<Body> bodyRepo,
                    SparePartSupplier<Engine> engRepo,
                    SparePartSupplier<Accessories> accRepo,
                    final int workersQuantity, final int delay) {
//    this.workers = Executors.newFixedThreadPool(workersQuantity);
    this.workers = new  CustomFixedThreadPool(workersQuantity);
    this.bodyRepo = bodyRepo;
    this.engRepo = engRepo;
    this.accRepo = accRepo;
    this.carRepo = carRepo;
    this.delay = delay;
    log.debug("init CarFactory workersQuantity:" + workersQuantity + " " + this.bodyRepo + " " + this.engRepo + " " + this.accRepo + " delay: " + this.delay);
  }

  public void shutdown() {
    this.workers.shutdownNow();
  }

  @Override
  public void requestCars(int orderSize) {
    Runnable task = () -> {
      Body body;
      Engine engine;
      Accessories acc;
      try {
        body = this.bodyRepo.getSparePart();
        engine = this.engRepo.getSparePart();
        acc = this.accRepo.getSparePart();
//        log.debug("factory delay is " + this.delay);
        Thread.sleep(this.delay);
      } catch (InterruptedException e) {
        return;
      }
      Car car = new Car(body, engine, acc);
      carRepo.acceptCar(car);
    };

    for (int i = 0; i < orderSize; ++i) {
      workers.submit(task);
    }
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
