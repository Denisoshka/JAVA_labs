package ru.nsu.zhdanov.lab_4.model.factory.interfaces;

public interface SparePartModelMonitorListener {
  void changed(int occupancy, int totalProduced);
}
