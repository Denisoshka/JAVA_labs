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
  requires java.desktop;

  opens ru.nsu.zhdanov.lab_3 to javafx.fxml;
  exports ru.nsu.zhdanov.lab_3;

  opens ru.nsu.zhdanov.lab_3.fx_facade to javafx.fxml, com.fasterxml.jackson.databind;
  exports ru.nsu.zhdanov.lab_3.fx_facade;

  //  opens javafx.beans.property to com.fasterxml.jackson.databind;

  exports ru.nsu.zhdanov.lab_3.model.main_model;
  opens ru.nsu.zhdanov.lab_3.model.game_context.entity to com.fasterxml.jackson.databind;
  opens ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels to com.fasterxml.jackson.databind;
  opens ru.nsu.zhdanov.lab_3.model.main_model to com.fasterxml.jackson.databind, javafx.fxml;
  exports ru.nsu.zhdanov.lab_3.model.game_context;
  opens ru.nsu.zhdanov.lab_3.model.game_context to com.fasterxml.jackson.databind, javafx.fxml;
  exports ru.nsu.zhdanov.lab_3.abstract_facade;
  opens ru.nsu.zhdanov.lab_3.abstract_facade to com.fasterxml.jackson.databind, javafx.fxml;
//  exports ru.nsu.zhdanov.lab_3;
//  opens ru.nsu.zhdanov.lab_3 to javafx.fxml;
}