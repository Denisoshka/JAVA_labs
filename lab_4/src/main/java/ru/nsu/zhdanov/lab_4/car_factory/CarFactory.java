package ru.nsu.zhdanov.lab_4.car_factory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab_4.parts_section.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab_4.parts_section.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.thread_pool.CustomThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CarFactory implements CarsRequest {
  final private CustomThreadPool workers;

  private @Setter SparePartSupplier<Body> bodyRep;
  private @Setter SparePartSupplier<Engine> engRep;
  private @Setter SparePartSupplier<Accessories> accRep;
  private @Setter CarConsumer<Car> carRepo;
  @Getter
  AtomicInteger delay;

  //todo make normal setter возможно при инициализации
  // объектов нужно взять ссылки на их методы и потом
  // уже через контроллер дергать

  //todo сделать здесь какой то метод склада наверное чтобы
  // когда мы изготовили товар уведомить

  public CarFactory(final int workersQuantity, final AtomicInteger delay) {
    log.info("init CarFactory");
    this.delay = delay;
    log.info("make CustomThreadPool");
    this.workers = new CustomThreadPool(workersQuantity);
  }

  public void shutdown() {
    log.info("shutdown");
    this.workers.shutdown();
  }

  @Override
  public void requestCars(int orderSize) {
    synchronized (workers) {
      for (int i = 0; i < orderSize; ++i) {
        Runnable task = () -> {
          log.info("call bodyRep.getSparePart()");
          Body body = (Body) bodyRep.getSparePart();
          log.info("call engRep.getSparePart()");
          Engine engine = (Engine) engRep.getSparePart();
          log.info("call accRep.getSparePart()");
          Accessories acc = (Accessories) accRep.getSparePart();
          try {
            Thread.sleep(delay.get());
          } catch (InterruptedException e) {
            return;
          }
          log.info("new Car(body, engine, acc) " + body.toString() + " " + engine.toString() + " " + acc.toString());
          Car car = new Car(body, engine, acc);
          log.info("call carRepo.acceptCar(car)");
          carRepo.acceptCar(car);
        };
//        todo нужно сделать notify на склад чтобы забрали эту херню как то так
        workers.execute(task);
      }
    }
  }
}
