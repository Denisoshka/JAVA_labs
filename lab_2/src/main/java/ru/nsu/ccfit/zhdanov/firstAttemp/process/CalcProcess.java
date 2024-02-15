package ru.nsu.ccfit.zhdanov.firstAttemp.process;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.CommandFactory;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.exception.CommandCreateException;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.CommandException;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@Slf4j
public class CalcProcess {
  CalcProcess() {
  }

  final static char kSkipLineSymbol = '#';

  public static void process(final String kInputPath, final String kOutputPath, final String CommandProperties) throws IOException {
    Writer out = (kOutputPath == null || kOutputPath.isEmpty()) ?
            new OutputStreamWriter(System.out)
            : new FileWriter(kOutputPath);
    Reader in = (kInputPath == null || kInputPath.isEmpty()) ?
            new InputStreamReader(System.in)
            : new FileReader(kInputPath);
    Context context = new Context(out);
    CommandFactory.makeInstance(CommandProperties);

    try (BufferedReader reader = new BufferedReader(in)) {
      String args;
      while ((args = reader.readLine()) != null) {
        if (args.isEmpty() || args.charAt(0) == kSkipLineSymbol) {
          continue;
        }

        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(args.split(" ")));
        String commandName = tokens.removeFirst();
        try {
          Command command = CommandFactory.getInstance().create(commandName);
          try {
            log.info("run command: \"" + commandName + "\" with args " + tokens);
            command.perform(tokens, context);
          } catch (CommandException e) {
            log.error("unable to run command: " + commandName, e);
          }
        } catch (CommandCreateException ignored) {
          log.error("unable to create command: " + commandName);
        }
      }
    }
  }
}


