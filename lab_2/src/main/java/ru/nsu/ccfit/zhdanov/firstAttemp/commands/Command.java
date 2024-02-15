package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import java.io.IOException;
import java.util.List;

public interface Command {
  void perform(List<String> args, ContextInterface context) throws IOException;
}
