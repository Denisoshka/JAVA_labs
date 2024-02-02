package ru.nsu.ccfit.zhdanov.firstAttemp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import ru.nsu.ccfit.zhdanov.firstAttemp.cliParser.CalcCliParser;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.CalcProcess;

@Slf4j
public class Main {

  public static void main(String[] args) throws Exception {
    final String kCommandProperties = "commands.properties";
    String kInput;
    String kOutput;

    try (CalcCliParser parser = new CalcCliParser()) {
      CommandLine commandLine = parser.parse(args);
      if (commandLine.hasOption("help")) {
        parser.printHelp();
        log.info("process finished by \"help\" call");
        return;
      }

      kInput = commandLine.getOptionValue("input");
      kOutput = commandLine.getOptionValue("output");

    }catch (Exception e){
      log.error("process finished with exception ", e);
      throw e;
    }

    log.info("start process with:");
    log.info("input=" + kInput);
    log.info("output=" + kOutput);
    log.info("commands properties=" + kCommandProperties);

    try {
      CalcProcess.process(kInput, kOutput, kCommandProperties);
    } catch (Exception e) {
      log.error("process finished with exception ", e);
      throw e;
    }
  }
}