module client {
  requires static lombok;

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
  requires org.slf4j;
  requires annotations;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires java.xml.bind;
  requires core;

  opens client.view to javafx.fxml;
  opens client.model.io_processing to javafx.fxml;
  opens client.model.main_context to javafx.fxml;
  opens client.model.main_context.interfaces to javafx.fxml;

  exports client.application to javafx.graphics;
  exports client.view;
  exports client.exception;
  exports client.model.io_processing;
  exports client.model.main_context;
  exports client.model.main_context.interfaces;
}