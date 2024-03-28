package ru.nsu.zhdanov.subtask9;

import ru.nsu.zhdanov.subtask9.implementation.Task;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    int n, k;

    try (Scanner scanner = new Scanner(System.in)) {
      n = scanner.nextInt();
      k = scanner.nextInt();
      try (Task task = new Task(n, k)) {
        while (scanner.hasNext() && Objects.equals(scanner.next(), "q")) {
          break;
        }
      }
    } catch (InputMismatchException e) {
      System.out.println("require 2 args for work");
    }
  }
}