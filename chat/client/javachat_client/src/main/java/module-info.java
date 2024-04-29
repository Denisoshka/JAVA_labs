module javachat_client.javachat_client {
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
  requires java.xml.bind;
  requires java.activation;


  opens javachat.client to javafx.fxml;
  exports javachat.client;
  exports javachat.client.view;
  exports javachat.client.exception;
  opens javachat.client.view to javafx.fxml;
  exports javachat.client.model;
  opens javachat.client.model to javafx.fxml;
  exports javachat.client.model.request_handler;
  opens javachat.client.model.request_handler to javafx.fxml;
  exports javachat.client.model.request_handler.requests;
  opens javachat.client.model.request_handler.requests to javafx.fxml;
  opens javachat.client.model.DTO.commands to java.xml.bind;
}