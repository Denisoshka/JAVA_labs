package ru.nsu.ccfit.zhdanov.firstAttemp.context.exception;

public class IncorrectVariable extends ContextException {
  public IncorrectVariable(final String varName) {
    super("Variable: "+ varName+ " incorrect");
  }
}
