package ru.nsu.zhdanov.lab4.car_factory;

public interface CarConsumer<Car> {
  abstract void acceptCar(Car car);
}
