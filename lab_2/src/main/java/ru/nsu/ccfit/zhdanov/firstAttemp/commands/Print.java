package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;

import java.io.IOException;
import java.util.List;

public class Print implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) throws IOException {
    context.print(context.peek());
  }
}
