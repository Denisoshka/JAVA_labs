package ru.nsu.ccfit.zhdanov.firstAttemp.commands;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.IncorrectParametersQuantity;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.ContextInterface;

import java.util.List;

public class Push implements Command {
  @Override
  public void perform(List<String> args, ContextInterface context) {
    if (args.isEmpty()){
      throw new IncorrectParametersQuantity(1, 0);
    }
    context.push(context.decode(args.getFirst()));
  }
}
