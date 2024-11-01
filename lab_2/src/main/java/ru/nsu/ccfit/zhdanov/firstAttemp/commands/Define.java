package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;

import java.util.List;

public class Define implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    if (args.size() != 2) {
      throw new IncorrectParametersQuantity(2, args.size());
    }
    context.define(args.getFirst(), args.getLast());
  }
}
