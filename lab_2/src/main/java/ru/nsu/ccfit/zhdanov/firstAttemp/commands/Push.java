package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectVariable;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.util.List;

public class Push implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    try {
      double a = context.decode(args.getFirst());
      context.push(a);
    } catch (NotContainVariable ignored) {
      throw new IncorrectVariable(args.getFirst());
    }
  }
}
