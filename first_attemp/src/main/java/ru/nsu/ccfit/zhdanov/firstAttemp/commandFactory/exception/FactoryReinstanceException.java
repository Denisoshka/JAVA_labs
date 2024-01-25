package ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception;

public class FactoryReinstanceException extends FactoryException{
  public FactoryReinstanceException() {
    super("factory already instanced");
  }
}
