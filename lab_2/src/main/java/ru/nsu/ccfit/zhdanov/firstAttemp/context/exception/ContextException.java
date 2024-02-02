package ru.nsu.ccfit.zhdanov.firstAttemp.context.exception;

public class ContextException extends RuntimeException {
  public ContextException(final String message) {
    super("ContextException" + message);
  }
}
