module ru.nsu.zhdanov.lab_4.lab_4 {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;
  requires com.almasb.fxgl.all;
  requires static lombok;

  opens ru.nsu.zhdanov.lab_4.lab_4 to javafx.fxml;
  exports ru.nsu.zhdanov.lab_4.lab_4;
  exports ru.nsu.zhdanov.lab_4.facade;
  opens ru.nsu.zhdanov.lab_4.facade to javafx.fxml;
}