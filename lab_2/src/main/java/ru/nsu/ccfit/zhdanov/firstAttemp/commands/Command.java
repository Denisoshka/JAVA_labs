package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.io.IOException;
import java.util.ArrayList;

public interface Command {
  void perform(ArrayList<String> args, Context context) throws IOException;
}
