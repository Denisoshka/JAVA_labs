package ru.nsu.zhdanov.lab_3.facade;

import java.util.Properties;

public interface SubControllerRequests {
  abstract void setContext(Properties properties, MainController controller);
  abstract void perform();
  abstract void shutdown();
}
