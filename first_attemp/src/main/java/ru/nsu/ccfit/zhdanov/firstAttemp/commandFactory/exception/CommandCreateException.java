package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class CommandCreateException extends FactoryException{
  public CommandCreateException(String message) {
    super("unable to create command " + message);
  }
}
