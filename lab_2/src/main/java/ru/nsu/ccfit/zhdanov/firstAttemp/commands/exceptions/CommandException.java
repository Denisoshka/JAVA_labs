package ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions;

public class CommandException extends RuntimeException{
  public CommandException(String message) {
    super("Command exception: " + message);
  }
}
