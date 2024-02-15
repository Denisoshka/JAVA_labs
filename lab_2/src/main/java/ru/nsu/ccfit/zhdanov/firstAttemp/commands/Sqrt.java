package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;

import java.util.List;

import static java.lang.Math.sqrt;

public class Sqrt implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    if (context.occupancy() < 1) {
      throw new IncorrectStackParametersQuantity(1, context.occupancy());
    }
    context.push(sqrt(context.pop()));
  }
}
