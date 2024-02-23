package ru.nsu.ccfit.zhdanov.firstAttemp.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.ccfit.zhdanov.firstAttemp.Factory.CommandCreateInterface;
import ru.nsu.ccfit.zhdanov.firstAttemp.Factory.CommandFactory;
import ru.nsu.ccfit.zhdanov.firstAttemp.Factory.exception.UnableToCreateCommand;
import ru.nsu.ccfit.zhdanov.firstAttemp.cashedFactory.CashedFactory;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.CommandException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class CalcProcess {
  final private static Logger log = LoggerFactory.getLogger(CalcProcess.class);

  public CalcProcess(final Properties commandProperties) {
    this.commandSupplier = new CashedFactory(new CommandFactory(commandProperties));
  }

  final private CommandCreateInterface commandSupplier;
  final static char kSkipLineSymbol = '#';

  public void process(final String kInputPath, final String kOutputPath) throws IOException {
    try (Writer out = (kOutputPath == null || kOutputPath.isEmpty())
            ? new OutputStreamWriter(System.out) : new FileWriter(kOutputPath);
         Reader in = (kInputPath == null || kInputPath.isEmpty())
                 ? new InputStreamReader(System.in) : new FileReader(kInputPath)) {
      Context context = new Context(out);
      try (BufferedReader reader = new BufferedReader(in)) {
        String args;
        while (Objects.nonNull(args = reader.readLine())) {
          if (args.isEmpty() || args.charAt(0) == kSkipLineSymbol) {
            continue;
          }
          ArrayList<String> tokens = new ArrayList<>(Arrays.asList(args.split(" ")));
          String commandName = tokens.removeFirst().toUpperCase();
          try {
            Command command = commandSupplier.create(commandName);
            try {
              log.info("Run command: \"" + commandName + "\" with args " + tokens);
              command.perform(tokens, context);
            } catch (CommandException e) {
              log.error("Unable to run command: " + commandName, e);
            }
          } catch (UnableToCreateCommand ignored) {
            log.error("Unable to create command: " + commandName);
          }
        }
      }
    }
  }
}


