package ru.nsu.ccfit.zhdanov.firstAttemp.cliParser;

import org.apache.commons.cli.*;

public class CalcCliParser {
  private final Options opts;

  public CalcCliParser() {
    CalcCliParser.class.getClassLoader();

    opts = new Options();
    opts.addOption(Option.builder()
                    .option("h")
                    .longOpt("help")
                    .desc("Print command help")
                    .build())
            .addOption(Option.builder()
                    .option("i")
                    .longOpt("input")
                    .desc("Input file")
                    .hasArg()
                    .argName("path")
                    .numberOfArgs(1)
                    .type(String.class)
                    .build())
            .addOption(Option.builder()
                    .option("o")
                    .longOpt("output")
                    .desc("Output file")
                    .hasArg()
                    .numberOfArgs(1)
                    .argName("path")
                    .type(String.class)
                    .build())
            .addOption(Option.builder()
                    .option("c")
                    .longOpt("config")
                    .desc("Config file for commands")
                    .hasArg()
                    .numberOfArgs(1)
                    .type(String.class)
                    .build()
            );
  }

  public CommandLine parse(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    return parser.parse(opts, args);
  }

  public void printHelp() {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("calculator", opts, true);
  }
}
