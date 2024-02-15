package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.io.IOException;
import java.util.List;

public interface Command {
  void perform(List<String> args, Context context) throws IOException;
}
