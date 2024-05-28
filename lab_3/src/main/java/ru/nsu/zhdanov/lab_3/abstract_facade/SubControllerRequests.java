package ru.nsu.zhdanov.lab_3.abstract_facade;

import ru.nsu.zhdanov.lab_3.fx_facade.MainController;

import java.util.Properties;

public interface SubControllerRequests {
  abstract void perform();

  abstract void shutdown();
}
