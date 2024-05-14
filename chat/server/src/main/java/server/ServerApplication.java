package server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exceptions.ServerConfigUnavailable;
import server.model.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServerApplication {
  static final private Logger log = LoggerFactory.getLogger(ServerApplication.class);
  static final private String CONFIG = "config";

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
    Properties serverProperties = new Properties();
    try (var reader = new BufferedReader(new FileReader(commandLine.getOptionValue(CONFIG)))) {
      serverProperties.load(reader);
    } catch (IOException e) {
      throw new ServerConfigUnavailable(commandLine.getOptionValue(CONFIG), e);
    }
    new Server(serverProperties).run();
    commandLine = null;
    cliParser = null;
  }
}