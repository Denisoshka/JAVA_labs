package ru.nsu.zhdanov.lab4.car_factory;

import lombok.Setter;
import ru.nsu.zhdanov.lab4.parts_section.SparePartRepository;
import ru.nsu.zhdanov.lab4.parts_section.SparePartSupplier;
import ru.nsu.zhdanov.lab4.parts_section.accessories_section.Accessories;
import ru.nsu.zhdanov.lab4.parts_section.body_section.Body;
import ru.nsu.zhdanov.lab4.parts_section.engine_section.Engine;
import ru.nsu.zhdanov.lab4.thread_pool.CustomThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

public class CarFactory<Car> implements OrderSupply {
  final private CustomThreadPool workers;

  private @Setter SparePartSupplier<Body> bodyRep;
  private @Setter SparePartSupplier<Engine> engRep;
  private @Setter SparePartSupplier<Accessories> accRep;

  @Setter
  AtomicInteger delay;

  CarFactory(final int workersQuantity, final int delay) {
    this.workers = new CustomThreadPool(workersQuantity);
  }


  @Override
  public void requestCars(int orderSize) {
    synchronized (workers) {
      for (int i = 0; i < orderSize; ++i) {
        Body body = bodyRep.getSparePart();
        Engine engine  = engRep.getSparePart();
        Accessories acc = accRep.getSparePart();
        Runnable task = (){
          
        }
//        todo нужно сделать notify на склад чтобы забрали эту херню как то так
        wo rkers.execute();
      }
    }

  }
}
