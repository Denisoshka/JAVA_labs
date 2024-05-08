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
//  requires com.fasterxml.jackson.annotation;
//  requires com.fasterxml.jackson.dataformat.xml;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires java.xml.bind;

  opens javachat.client.model.dto.subtypes to java.xml.bind;
  opens javachat.client.model.dto to java.xml.bind;
//  exports javachat.client.model.DTO.events;
//  exports javachat.client.model.DTO.commands;
  opens javachat.client to javafx.fxml;
  exports javachat.client;
  exports javachat.client.view;
  exports javachat.client.exception;
  opens javachat.client.view to javafx.fxml;
  opens javachat.client.model to javafx.fxml;
  exports javachat.client.model.io_processing;
  opens javachat.client.model.io_processing to javafx.fxml;
  opens javachat.client.model.dto.exceptions to java.xml.bind;
  opens javachat.client.model.dto.interfaces to java.xml.bind;
  exports javachat.client.model.main_context;
  opens javachat.client.model.main_context to javafx.fxml;
}