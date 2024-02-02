package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.util.ArrayList;

public class Division implements Command{
  @Override
  public void perform(ArrayList<String> args, Context context) {
    if (context.capacity() < 2) {
      throw new IncorrectStackParametersQuantity(2, context.capacity());
    }
    context.push(context.pop()/context.pop());
  }
}
