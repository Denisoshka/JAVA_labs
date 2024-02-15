package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;

import java.util.List;

public class Addition implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    if (context.occupancy() < 2) {
      throw new IncorrectStackParametersQuantity(2, context.occupancy());
    }
    context.push(context.pop() + context.pop());
  }
}
