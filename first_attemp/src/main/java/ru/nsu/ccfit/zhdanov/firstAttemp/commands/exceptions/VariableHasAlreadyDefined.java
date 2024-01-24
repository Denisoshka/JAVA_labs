package ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions;

public class VariableHasAlreadyDefined extends CommandException{
  public VariableHasAlreadyDefined(String varName) {
    super("Variable" + varName + "has already defined");
  }
}
