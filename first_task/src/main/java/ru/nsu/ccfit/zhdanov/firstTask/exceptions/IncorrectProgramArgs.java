package ru.nsu.ccfit.zhdanov.firstTask.exceptions;

public class IncorrectProgramArgs extends RuntimeException {
  public IncorrectProgramArgs() {
    super("Incorrect program args");
  }
}
