package ru.nsu.zhdanov.lab_4.parts_section;

public interface SparePartConsumer <SparePartT extends SparePart>{
  abstract void acceptCar(SparePartT car);
}
