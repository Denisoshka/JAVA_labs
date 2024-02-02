package ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions;

public class IncorrectVariable extends CommandException{
  public IncorrectVariable(String message) {
    super("Incorrect defined variable " + message);
  }
}
