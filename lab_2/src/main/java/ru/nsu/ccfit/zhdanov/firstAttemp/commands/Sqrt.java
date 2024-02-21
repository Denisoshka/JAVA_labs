package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;

import java.util.List;

import static java.lang.Math.sqrt;

public class Sqrt implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    context.push(sqrt(context.pop()));
  }
}
