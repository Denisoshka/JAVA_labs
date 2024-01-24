package ru.nsu.ccfit.zhdanov.firstAttemp.process.exception;

public class CalcProcessException extends RuntimeException {
  public CalcProcessException(String message) {
    super("Calc process exception: " + message);
  }
}
