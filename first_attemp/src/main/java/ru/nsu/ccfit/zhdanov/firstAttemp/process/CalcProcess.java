package ru.nsu.ccfit.zhdanov.firstAttemp.process;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.zhdanov.firstAttemp.commandFactory.CommandFactory;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.Command;
import ru.nsu.ccfit.zhdanov.firstAttemp.commands.exceptions.CommandException;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.exception.UnavailableInputFile;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.exception.UnavailableOutputFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@Slf4j
public class CalcProcess {
  CalcProcess() {
  }

  final static char kSkipLineSymbol = '#';

  public static void process(final String kInputPath, final String kOutputPath, final String CommandProperties) {
    InputStream in;
    OutputStream out;
    Context context;
    try {
      out = (kOutputPath == null) ? System.out : new FileOutputStream(kOutputPath);
    } catch (IOException e) {
      throw new UnavailableOutputFile();
    }
    try {
      in = (kInputPath == null) ? System.in : new FileInputStream(kInputPath);
    } catch (IOException e) {
      throw new UnavailableInputFile(kInputPath);
    }

    /*try {
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }*/
    CommandFactory.makeInstance(CommandProperties);
    context = new Context(out);
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
            command.run(args, context);
            log.info("run command: " + commandName + " with args " + args);
          } catch (CommandException e) {
            log.error("unable to run command: " + commandName, e);
          }
        } catch (Exception ignored) {
          log.error("unable to create command: " + commandName);
        }
      }
    }
    /*catch (Exception e) {
      log.error(e.getMessage());
    }*/
  }
}


