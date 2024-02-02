package ru.nsu.zhdanov.lab4.car_factory;

import ru.nsu.zhdanov.lab4.thread_pool.CustomThreadPool;

public class CarFactory<Car> implements OrderSupply {
  final CustomThreadPool workers;
  CarFactory(final int workersQuantity) {
    this.workers = new CustomThreadPool(workersQuantity);
  }


  @Override
  public void requestCars(int orderSize) {
    synchronized (workers){
      for (int i = 0; i < orderSize; ++i){




        workers.execute();
      }
    }

  }
}
