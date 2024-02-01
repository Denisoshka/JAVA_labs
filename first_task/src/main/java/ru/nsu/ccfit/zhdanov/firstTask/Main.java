package ru.nsu.ccfit.zhdanov.firstTask;

import lombok.val;
import ru.nsu.ccfit.zhdanov.firstTask.exceptions.IncorrectProgramArgs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    if (args.length != 1 || args[0].isEmpty()) {
      throw new IncorrectProgramArgs();
    }
    Map<String, Integer> tokensFreq = new HashMap<>();
    try (val scanner = new Scanner(new FileInputStream(args[0]))) {
      while (scanner.hasNextLine()) {
        val line = scanner.nextLine().split("\\P{Alnum}+");
        for (val token : line) {
          if (!token.isEmpty()) {
            val lowToken = token.toLowerCase();
            tokensFreq.put(lowToken, tokensFreq.getOrDefault(lowToken, 0) + 1);
          }
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return;
    }

    try (FileWriter csvWriter = new FileWriter("out.csv")) {
      int totalWordsQuantity = tokensFreq.values().stream().mapToInt(Integer::intValue).sum();
      tokensFreq.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered((entry) -> {
        try {
          csvWriter.write(String.format("%s,%d,%s%n", entry.getKey(), entry.getValue(), String.
                  valueOf((float) entry.getValue() / totalWordsQuantity * 100).replace(',', '.')));
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      });
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
