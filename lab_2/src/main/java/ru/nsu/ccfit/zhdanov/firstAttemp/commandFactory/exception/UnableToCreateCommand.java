package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class UnableToCreateCommand extends FactoryException{
  public UnableToCreateCommand(String message) {
    super("unable to create command " + message);
  }
}
