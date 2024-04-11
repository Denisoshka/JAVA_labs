package ru.nsu.zhdanov.lab_4.model.factory.interfaces;

public interface SparePartConsumer<SparePartT> {
  abstract void acceptSparePart(SparePartT car) throws InterruptedException;
}
