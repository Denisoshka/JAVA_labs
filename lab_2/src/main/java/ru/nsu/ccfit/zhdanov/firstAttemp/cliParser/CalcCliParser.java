package ru.nsu.ccfit.zhdanov.firstAttemp.cliParser;

import lombok.Getter;
import org.apache.commons.cli.*;

public class CalcCliParser implements AutoCloseable {
  private final Options opts;

  public CalcCliParser() {
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
                    .build());
  }

  public CommandLine parse(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    return parser.parse(opts, args);
  }

  public void printHelp(){
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("calc", opts, true);
  }

  @Override
  public void close() throws Exception {
  }
}
