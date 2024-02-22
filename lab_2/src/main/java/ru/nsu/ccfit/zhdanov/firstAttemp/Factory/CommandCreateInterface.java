package ru.nsu.ccfit.zhdanov.firstAttemp.Factory;

import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;

public interface CommandCreateInterface {
  abstract Command create(final String command);
}
