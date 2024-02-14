module ru.nsu.zhdanov.lab_3.lab_3 {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.bootstrapfx.core;
  requires com.almasb.fxgl.all;
  requires static lombok;

  opens ru.nsu.zhdanov.lab_3.lab_3 to javafx.fxml;
  exports ru.nsu.zhdanov.lab_3.lab_3;
}