package ru.nsu.ccfit.zhdanov.firstAttemp.process.exception;

import ru.nsu.ccfit.zhdanov.firstAttemp.process.CalcProcess;

public class UnavailableOutputFile extends CalcProcessException {
  public UnavailableOutputFile () {
    super("Unavailable output file");
  }
}
