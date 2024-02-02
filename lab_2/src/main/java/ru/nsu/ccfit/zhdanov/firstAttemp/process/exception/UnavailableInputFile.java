package ru.nsu.ccfit.zhdanov.firstAttemp.process.exception;

public class UnavailableInputFile extends CalcProcessException {
  public UnavailableInputFile(String message) {
    super("Unavailable input file: " + message);
  }
}

