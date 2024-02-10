package ru.nsu.zhdanov.lab_4.parts_section;

public interface SparePartConsumer<SparePartT> {
  abstract void acceptSparePart(SparePartT car);
}
