package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;

import java.util.List;

public class Subtraction implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    context.push(context.pop() - context.pop());
  }
}
