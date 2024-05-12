module client {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
  requires static lombok;
  requires org.slf4j;
  requires annotations;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires java.xml.bind;
  requires core;

  exports client.view;
  exports client.exception;
  opens client.view to javafx.fxml;
  exports client.model.io_processing;
  opens client.model.io_processing to javafx.fxml;
  exports client.model.main_context;
  opens client.model.main_context to javafx.fxml;
  exports client.model.main_context.interfaces;
  opens client.model.main_context.interfaces to javafx.fxml;
}