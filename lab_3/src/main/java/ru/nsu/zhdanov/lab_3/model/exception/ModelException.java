package ru.nsu.zhdanov.lab_3.model.exception;

public class ModelException extends RuntimeException{
  public ModelException(String message, Throwable cause) {
    super(message, cause);
  }
}
