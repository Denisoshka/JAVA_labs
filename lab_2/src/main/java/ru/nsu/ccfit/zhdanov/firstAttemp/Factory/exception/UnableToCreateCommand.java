package ru.nsu.ccfit.zhdanov.firstAttemp.Factory.exception;

public class UnableToCreateCommand extends FactoryException{
  public UnableToCreateCommand(String message) {
    super("unable to create command " + message);
  }
}
