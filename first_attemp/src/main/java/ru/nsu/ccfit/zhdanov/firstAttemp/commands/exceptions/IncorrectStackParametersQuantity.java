package ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions;

public class IncorrectStackParametersQuantity extends CommandException {
  public IncorrectStackParametersQuantity(final int required, final int available) {
    super("Incorrect stack parameters quantity(required=" + String.valueOf(required) + " " + "available=" + String.valueOf(available) + ")");
  }
}
