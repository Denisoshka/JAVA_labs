package ru.nsu.ccfit.zhdanov.firstAttemp.cashedFactory;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.Factory.CommandCreateInterface;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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
      log.info("get new command " + command);
      return performer;
    }
    log.info("get from cashe command: " + command);
    return performer;
  }
}
