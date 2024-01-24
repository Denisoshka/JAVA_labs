package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.util.ArrayList;

public interface Command {
//  DefaultCommand(ArrayList<String> args, Context context);
  void run(ArrayList<String> args, Context context);
}
