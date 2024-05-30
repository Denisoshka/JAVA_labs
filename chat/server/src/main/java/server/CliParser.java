package server;

import org.apache.commons.cli.*;

public class CliParser {
  private final Options opts;

  public CliParser() {
    CliParser.class.getClassLoader();

    opts = new Options();
    opts.addOption(Option.builder()
                    .option("h")
                    .longOpt("help")
                    .desc("Print command help")
                    .build())
            .addOption(Option.builder()
                    .option("p")
                    .longOpt("port")
                    .desc("server port")
                    .hasArg()
                    .numberOfArgs(1)
                    .type(String.class)
                    .build())
            .addOption((Option.builder()
                    .option("h")
                    .longOpt("host")
                    .desc("server host").hasArg()
                    .numberOfArgs(1)
                    .type(String.class)
                    .build())
            );
  }

  public CommandLine parse(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    return parser.parse(opts, args);
  }

  public void printHelp() {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("sever", opts, true);
  }
}
