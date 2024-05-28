module ru.nsu.zhdanov.lab_4.lab_4 {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.slf4j; // Добавьте эту строку для включения модуля org.slf4j

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;
  requires com.almasb.fxgl.all;
  requires static lombok;
  requires annotations;
  requires ch.qos.logback.classic;
  requires ch.qos.logback.core;

  opens ru.nsu.zhdanov.lab_4.lab_4 to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.lab_4;
  exports ru.nsu.zhdanov.lab_4.facade;
  opens ru.nsu.zhdanov.lab_4.facade to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.model;
  opens ru.nsu.zhdanov.lab_4.model to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.model.factory.interfaces;
  opens ru.nsu.zhdanov.lab_4.model.factory.interfaces to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.model.factory.raw_classes;
  opens ru.nsu.zhdanov.lab_4.model.factory.raw_classes to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.model.factory.accessories_section;
  opens ru.nsu.zhdanov.lab_4.model.factory.accessories_section to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.model.factory.body_section;
  opens ru.nsu.zhdanov.lab_4.model.factory.body_section to javafx.fxml;
}