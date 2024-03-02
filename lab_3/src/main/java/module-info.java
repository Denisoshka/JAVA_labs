module ru.nsu.zhdanov.lab_3.lab_3 {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.slf4j;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.bootstrapfx.core;
  requires com.almasb.fxgl.all;
  requires static lombok;
  requires com.fasterxml.jackson.databind;

  opens ru.nsu.zhdanov.lab_3.lab_3 to javafx.fxml;
  exports ru.nsu.zhdanov.lab_3.lab_3;
  opens ru.nsu.zhdanov.lab_3.facade to javafx.fxml;
  exports ru.nsu.zhdanov.lab_3.facade;
}