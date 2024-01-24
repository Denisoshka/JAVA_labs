package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.VariableHasAlreadyDefined;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.util.ArrayList;

public class Define implements Command {
  @Override
  public void run(ArrayList<String> args, Context context) {
    if (args.size() != 2) {
      throw new IncorrectStackParametersQuantity(2, args.size());
    }
    try {
      context.peekVariable(args.getFirst());
      throw new VariableHasAlreadyDefined(args.getFirst());
    } catch (NotContainVariable ignored) {
      context.pushVariable(args.getFirst(), Double.valueOf(args.get(1)));
    }
  }
}
