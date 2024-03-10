package ru.nsu.zhdanov.lab_3.model.game_context.entity_factory.exception;

public class FactoryException extends RuntimeException {
  public FactoryException(final String message) {
    super("Factory exception: " + message);
  }
}
