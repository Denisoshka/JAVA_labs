package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.VariableHasAlreadyDefined;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.util.List;

public class Define implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    if (args.size() != 2) {
      throw new IncorrectStackParametersQuantity(2, args.size());
    }
    context.define(args.getFirst(), Double.parseDouble(args.getLast()));
  }
}
