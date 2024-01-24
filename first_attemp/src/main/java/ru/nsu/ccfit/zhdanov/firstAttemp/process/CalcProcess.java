package ru.nsu.ccfit.zhdanov.firstAttemp.process;

import ru.nsu.ccfit.zhdanov.firstAttemp.context.Context;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.exception.UnavailableInputFile;
import ru.nsu.ccfit.zhdanov.firstAttemp.process.exception.UnavailableOutputFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class CalcProcess {
  final static char kSkipLineSymbol = '#';

  public static void process(final String kInputPath, final String kOutputPath) {
    InputStream in;
    OutputStream out;
    Context context;
    try {
      out = (kOutputPath == null) ? System.out : new FileOutputStream(kOutputPath);
    } catch (IOException e) {
      throw new UnavailableOutputFile();
    }
    try {
      in = (kInputPath == null) ? System.in : new FileInputStream(kInputPath);
    } catch (IOException e) {
      throw new UnavailableInputFile();
    }

    context = new Context(out);
    try (Scanner scanner = new Scanner(in)) {
      while (scanner.hasNextLine()) {
        String ArgsLine = scanner.nextLine();
        if (ArgsLine.charAt(0) == kSkipLineSymbol) {
          continue;
        }
        ArrayList<String> args = new ArrayList<>(Arrays.asList(ArgsLine.split(" ")));
        if (args.isEmpty()) {
          continue;
        }
      }
    }
  }

  private class CommandQueue {
    InputStream in;

    public CommandQueue(InputStream in) {
      this.in = in;
//      taskPool = new LinkedList<>();
    }

    public boolean isEmpty() {
      try () {
        while () {
        }
      }
      return taskPool.isEmpty();
    }

    public ArrayList<String> poll() {
      return taskPool.poll();
    }
  }
}


