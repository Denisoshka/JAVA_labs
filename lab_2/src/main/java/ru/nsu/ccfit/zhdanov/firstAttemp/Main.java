package ru.nsu.ccfit.zhdanov.firstAttemp;

import org.apache.commons.cli.CommandLine;
import ru.nsu.ccfit.zhdanov.firstAttemp.cliParser.CalcCliParser;
import ru.nsu.ccfit.zhdanov.firstAttemp.properties_loader.PropertiesLoader;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.CalcProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
  static final private Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {

    CalcCliParser parser = new CalcCliParser();
    CommandLine commandLine = parser.parse(args);
    if (commandLine.hasOption("help")) {
      parser.printHelp();
      log.info("process finished by \"help\" call");
      return;
    }
    String commandsProperties = commandLine.getOptionValue("config", "commands.properties");
    String input = commandLine.getOptionValue("input");
    String output = commandLine.getOptionValue("output");

    log.info("start process with:");
    log.info("input=" + input);
    log.info("output=" + output);
    log.info("commands properties=" + commandsProperties);

    try {
      CalcProcess calc = new CalcProcess(PropertiesLoader.load(commandsProperties));
      calc.process(
              commandLine.getOptionValue("input"),
              commandLine.getOptionValue("output")
      );
    } catch (Exception e) {
      log.error("process finished with exception ", e);
      throw e;
    }
  }
}