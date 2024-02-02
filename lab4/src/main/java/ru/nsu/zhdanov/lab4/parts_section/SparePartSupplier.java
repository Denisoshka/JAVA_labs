package ru.nsu.zhdanov.lab4.parts_section;

public interface SparePartSupplier<SparePartT extends SparePart> {
  abstract SparePartT getSparePart();
}