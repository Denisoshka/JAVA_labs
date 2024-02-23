package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;

import java.io.IOException;
import java.util.List;

/**
 * Delete top of context stack
 */
public class Pop implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) throws IOException {
    context.pop();
  }
}
