package ru.nsu.ccfit.zhdanov.firstAttemp.context.exception;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.CommandException;

public class ContextException extends CommandException {
  public ContextException(final String message) {
    super("ContextException" + message);
  }
}
