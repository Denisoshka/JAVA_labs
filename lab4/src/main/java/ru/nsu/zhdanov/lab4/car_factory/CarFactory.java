package ru.nsu.zhdanov.lab4.car_factory;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.Accessories;
import ru.nsu.zhdanov.lab4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine;
import ru.nsu.zhdanov.lab4.thread_pool.CustomThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

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
    this.delay = delay;
    this.workers = new CustomThreadPool(workersQuantity);
  }


  @Override
  public void requestCars(int orderSize) {
    synchronized (workers) {
      for (int i = 0; i < orderSize; ++i) {
        Runnable task = () -> {
          Body body = bodyRep.getSparePart();
          Engine engine = engRep.getSparePart();
          Accessories acc = accRep.getSparePart();
          try {
            Thread.sleep(delay.get());
          } catch (InterruptedException e) {
            return;
          }
          Car car = new Car(body, engine, acc);
          carRepo.acceptCar(car);
        };
//        todo нужно сделать notify на склад чтобы забрали эту херню как то так
        workers.execute(task);
      }
    }
  }
}
