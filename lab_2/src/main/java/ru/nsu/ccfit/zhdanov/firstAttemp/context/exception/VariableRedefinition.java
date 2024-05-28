package ru.nsu.ccfit.zhdanov.firstAttemp.context.exception;

public class VariableRedefinition extends ContextException {
  public VariableRedefinition(final String varName) {
    super("Redefinition of variable" + varName);
  }
}
