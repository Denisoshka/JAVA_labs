package ru.nsu.ccfit.zhdanov.firstAttemp.cashedFactory;

import ru.nsu.ccfit.zhdanov.firstAttemp.Factory.CommandCreateInterface;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;

import java.util.HashMap;
import java.util.Map;

public class CashedFactory implements CommandCreateInterface {
  final CommandCreateInterface supplier;
  final Map<String, Command> cashe;


  public CashedFactory(CommandCreateInterface supplier) {
    this.supplier = supplier;
    this.cashe = new HashMap<>();
  }

  @Override
  public Command create(String command) {
    Command performer = cashe.get(command);
    if (performer == null) {
      performer = supplier.create(command);
      cashe.put(command, performer);
      return performer;
    }
    return performer;
  }
}
