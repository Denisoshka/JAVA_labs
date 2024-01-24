package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

//package ru.nsu.ccfit.petrov.task2.creator.exception;

/**
 * {@code ContextException} is superclass of those exceptions that can be thrown during working with command creator
 */

public class FactoryException extends RuntimeException {
  public FactoryException(String message) {
    super("Factory exception: " + message);
  }
}
