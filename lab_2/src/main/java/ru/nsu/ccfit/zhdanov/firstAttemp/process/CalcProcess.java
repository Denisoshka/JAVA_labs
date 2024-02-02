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
    Writer out = (kOutputPath.isEmpty()) ? new OutputStreamWriter(System.out) : new FileWriter(kOutputPath);
    Reader in = (kInputPath.isEmpty()) ? new InputStreamReader(System.in) : new FileReader(kInputPath);
    Context context = new Context(out);
    CommandFactory.makeInstance(CommandProperties);

    try (Scanner scanner = new Scanner(in)) {
      while (scanner.hasNextLine()) {
        String ArgsLine = scanner.nextLine();
        if (ArgsLine.isEmpty() || ArgsLine.charAt(0) == kSkipLineSymbol) {
          continue;
        }

        ArrayList<String> args = new ArrayList<>(Arrays.asList(ArgsLine.split(" ")));
        String commandName = args.removeFirst();
        try {
          Command command = CommandFactory.getInstance().create(commandName);
          try {
            command.perform(args, context);
            log.info("run command: " + commandName + " with args " + args);
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


