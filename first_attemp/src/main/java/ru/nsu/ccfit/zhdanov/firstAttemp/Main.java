package ru.nsu.ccfit.zhdanov.firstAttemp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.nsu.ccfit.zhdanov.firstAttemp.cliParser.CliParser;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.CalcProcess;

@Slf4j
public class Main {

  public static void main(String[] args) {
    CommandLine commandLine;
    try {
      commandLine = CliParser.parse(args);
    } catch (ParseException e) {
      System.out.printf(e.getMessage());
      return;
    }

    final String kInput = commandLine.getOptionValue("input");
    final String kOutput = commandLine.getOptionValue("output");
    final String kCommandProperties = "commands.properties";
    log.info("start process with");
    log.info("input="+ kInput);
    log.info("output="+ kOutput);
    log.info("command properties="+ kCommandProperties);

    try {
      CalcProcess.process(kInput, kOutput, kCommandProperties);
    } catch (Exception e) {
      log.info("process finished with exception ", e);
    }
  }
}