package ru.nsu.ccfit.zhdanov.firstAttemp.process;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.Factory;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.UnableToCreateCommand;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.interfaces.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.ContextException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class CalcProcess {
  public CalcProcess(final Properties commandProperties) {
    this.factory = new Factory<>(commandProperties);
  }

  final private Factory<Command> factory;
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
          String commandName = tokens.removeFirst();
          try {
            Command command = factory.create(commandName);
            try {
              log.info("Run command: \"" + commandName + "\" with args " + tokens);
              command.perform(tokens, context);
            } catch (ContextException e) {
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


