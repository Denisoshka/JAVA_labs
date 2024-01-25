package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.NotContainVariable;

import java.util.ArrayList;

public class Push implements Command {
  @Override
  public void run(ArrayList<String> args, Context context) {
    try {
      double a = context.peekVariable(args.getFirst());
      context.push(a);
    } catch (NotContainVariable ignored) {
      double a = Double.parseDouble(args.getFirst());
      context.push(a);
    }
  }
}
