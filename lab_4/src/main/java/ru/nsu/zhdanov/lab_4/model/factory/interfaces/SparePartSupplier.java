package ru.nsu.zhdanov.lab_4.model.factory.interfaces;

public interface SparePartSupplier<SparePartT> {
  abstract SparePartT getSparePart() throws InterruptedException;
}