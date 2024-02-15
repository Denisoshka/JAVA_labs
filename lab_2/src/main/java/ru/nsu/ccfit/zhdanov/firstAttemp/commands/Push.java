package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectVariable;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.util.List;

public class Push implements Command {
  @Override
  public void perform(List<String> args, Context context) {
    try {
      double a = context.peekVariable(args.getFirst());
      context.push(a);
    } catch (NotContainVariable ignored) {
      double a;
      try {
         a = Double.parseDouble(args.getFirst());
      }catch (NumberFormatException e){
        throw new IncorrectVariable(args.getFirst());
      }
      context.push(a);
    }
  }
}
