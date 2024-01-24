package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.util.ArrayList;

public class Print implements Command {
  @Override
  public void run(ArrayList<String> args, Context context) {
    double a = context.peek();
    context.print(a);
  }
}
