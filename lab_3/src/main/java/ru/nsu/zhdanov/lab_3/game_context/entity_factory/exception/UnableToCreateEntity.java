package ru.nsu.zhdanov.lab_3.game_context.entity_factory.exception;

public class UnableToCreateEntity extends FactoryException{
  public UnableToCreateEntity(String message) {
    super("unable to create command " + message);
  }
}
