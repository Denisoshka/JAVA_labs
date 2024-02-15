package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectStackParametersQuantity;

import java.io.IOException;
import java.util.List;

/**
 * desc: delete top of context stack and return this value
 * arguments: none
 */
public class Pop implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) throws IOException {
    if (args.size() != 1) {
      throw new IncorrectStackParametersQuantity(1, args.size());
    }
    context.pop();
  }
}
