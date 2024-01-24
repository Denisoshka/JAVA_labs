package ru.nsu.ccfit.zhdanov.firstAttemp.cliParser;

//import org.apache.commons.cli.CommandLineParser;

import org.apache.commons.cli.*;

public class CliParser {
  public static CommandLine parse(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    Options opts = new Options();
//    todo make HelpFormatter
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
                    .build());
    return parser.parse(opts, args);
  }
}
