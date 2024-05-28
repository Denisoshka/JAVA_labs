package ru.nsu.zhdanov.lab_4.model.dealer_repository;

import ru.nsu.zhdanov.lab_4.model.factory.factory_section.Car;

public interface CarSellListener {
  void produced(String dealer, Car car);
}
