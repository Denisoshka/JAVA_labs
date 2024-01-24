package ru.nsu.ccfit.zhdanov.firstAttemp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.nsu.ccfit.zhdanov.firstAttemp.cliParser.CliParser;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.CalcProcess;

import java.util.logging.Logger;


public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    CommandLine commandLine;
    try {
      commandLine = CliParser.parse(args);
    } catch (ParseException e) {
      System.out.printf(e.getMessage());
      return;
    }

    final String input = commandLine.getOptionValue("input");
    final String output = commandLine.getOptionValue("output");
    CalcProcess.process(input, output);
  }
}