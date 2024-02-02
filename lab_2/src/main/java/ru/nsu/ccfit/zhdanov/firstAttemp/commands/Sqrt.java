package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class Sqrt implements Command {
  @Override
  public void perform(ArrayList<String> args, Context context) {
    if (context.capacity() < 1) {
      throw new IncorrectStackParametersQuantity(1, context.capacity());
    }
    context.push(sqrt(context.pop()));
  }
}
