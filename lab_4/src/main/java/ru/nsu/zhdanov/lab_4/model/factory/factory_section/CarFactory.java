package ru.nsu.zhdanov.lab_4.model.factory.factory_section;

import org.slf4j.Logger;
import ru.nsu.zhdanov.lab_4.model.factory.accessories_section.Accessories;
import ru.nsu.zhdanov.lab_4.model.factory.body_section.Body;
import ru.nsu.zhdanov.lab_4.model.factory.engine_section.Engine;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.MonitorListenerIntroduction;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartModelMonitorListener;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartSupplier;
import ru.nsu.zhdanov.lab_4.thread_pool.CustomFixedThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class CarFactory implements CarsRequest, MonitorListenerIntroduction {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(CarFactory.class.getName());
  private final ExecutorService workers;
  private final SparePartSupplier<Body> bodyRepo;
  private final SparePartSupplier<Engine> engRepo;
  private final SparePartSupplier<Accessories> accRepo;
  private final CarRepository carRepo;
  private volatile int delay;

  private final List<SparePartModelMonitorListener> listeners = new ArrayList<>(1);
  private final AtomicInteger totalProduced = new AtomicInteger(0);

  public CarFactory(CarRepository carRepo, SparePartSupplier<Body> bodyRepo,
                    SparePartSupplier<Engine> engRepo,
                    SparePartSupplier<Accessories> accRepo,
                    final int workersQuantity, final int delay) {
    this.workers = new CustomFixedThreadPool(workersQuantity);
    this.bodyRepo = bodyRepo;
    this.engRepo = engRepo;
    this.accRepo = accRepo;
    this.carRepo = carRepo;
    this.delay = delay;
//    log.s
  }

  public void shutdown() {
    this.workers.shutdownNow();
  }

  @Override
  public void requestCars(int orderSize) {
    Runnable task = () -> {
      try {
        Body body = this.bodyRepo.getSparePart();
        Engine engine = this.engRepo.getSparePart();
        Accessories acc = this.accRepo.getSparePart();
        Thread.sleep(this.delay);
        Car car = new Car(body, engine, acc);
        log.trace("new car: " + car);
        carRepo.acceptCar(car);
        onCarProduced();
      } catch (InterruptedException ignored) {
      }
    };
    for (int i = 0; i < orderSize; ++i) {
      workers.submit(task);
    }
  }

  @Override
  public void addProduceMonitorListener(SparePartModelMonitorListener listener) {
    listeners.add(listener);
  }

  private void onCarProduced() {
    for (var listener : listeners) listener.changed(carRepo.occupancy(), totalProduced.incrementAndGet());
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
