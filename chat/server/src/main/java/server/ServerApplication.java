package server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.model.Server;

import java.io.IOException;

public class ServerApplication {
  private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);

  private static final String PORT = "port";
  private static final String HOST = "host";

  public static void main(String[] args) throws IOException {
    CommandLine commandLine;
    CliParser cliParser = new CliParser();
    try {
      commandLine = cliParser.parse(args);
      if (commandLine.hasOption("help")) {
        cliParser.printHelp();
        log.info("process finished by \"help\" call");
        return;
      }
    } catch (ParseException e) {
      cliParser.printHelp();
      log.info("process finished by parse args ex", e);
      return;
    }
    new Server(commandLine.getOptionValue(HOST), commandLine.getOptionValue(PORT)).run();
    commandLine = null;
    cliParser = null;
  }
}