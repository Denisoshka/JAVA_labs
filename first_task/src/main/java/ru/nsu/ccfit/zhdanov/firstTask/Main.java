package ru.nsu.ccfit.zhdanov.firstTask;

import ru.nsu.ccfit.zhdanov.firstTask.exceptions.IncorrectProgramArgs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Main {
  public static void main(String[] args) {
    if (args.length != 1 || args[0].isEmpty()) {
      throw new IncorrectProgramArgs();
    }

    Map<String, Integer> tokensFreq = new HashMap<>();
    try (BufferedReader input = new BufferedReader(new FileReader(args[0]))) {
      String line;
      while ((line = input.readLine()) != null) {
        for (String token : line.split("\\P{Alnum}+")) {
          if (!token.isEmpty()) {
            token = token.toLowerCase();
            tokensFreq.put(token, tokensFreq.getOrDefault(token, 0) + 1);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace(System.err);
      return;
    }

    try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter("out.csv"))) {
      int totalWordsQuantity = tokensFreq.values().stream().mapToInt(Integer::intValue).sum();
      String key;
      int value;
      for (Map.Entry<String, Integer> pair : tokensFreq.entrySet().stream().sorted(Map.Entry.comparingByValue()).toArray(Map.Entry[]::new)) {
        key = pair.getKey();
        value = pair.getValue();
        try {
          csvWriter.write(String.format("%s,%d,%s%n",key , value, String.
                  valueOf((float) value / totalWordsQuantity * 100).replace(',', '.')));
        } catch (IOException e) {
          e.printStackTrace(System.err);
          return;
        }
      }
      csvWriter.flush();
    }catch ( IOException e ){
      e.printStackTrace(System.err);
    }
  }
}
