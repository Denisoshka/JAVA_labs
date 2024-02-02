package ru.nsu.ccfit.zhdanov.firstAttemp.context.exception;

public class NotContainVariable extends ContextException{
  public NotContainVariable(String message) {
    super("Not contain variable" + message);
  }
}
