package ru.nsu.zhdanov.lab_3.model.exception;

public class DumpScoreException extends ModelException {
  public DumpScoreException(Throwable cause) {
    super("Unable to dump score", cause);
  }
}
