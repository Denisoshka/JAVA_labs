package javachat.server;

import javachat.server.exceptions.ServerConfigUnavailable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ServerStart {
  static final private Logger log = LoggerFactory.getLogger(ServerStart.class);
  static final private String CONFIG = "config";

  public static void main(String[] args) {
    CommandLine commandLine;
    CliParser cliParser = new CliParser();
    try {
      commandLine = cliParser.parse(args);
      if (commandLine.hasOption("help")) {
        cliParser.printHelp();
        log.info("process finished by \"help\" call");
        return;
      }
    } catch (ParseException ignored) {
      cliParser.printHelp();
      log.info("process finished by parse args ex");
      return;
    }
    Properties serverProperties = new Properties();
    try (var reader = new BufferedReader(new FileReader(commandLine.getOptionValue(CONFIG)))) {
      serverProperties.load(reader);
    } catch (IOException e) {
      throw new ServerConfigUnavailable(commandLine.getOptionValue(CONFIG), e);
    }
    commandLine = null;
    cliParser = null;
  }
}