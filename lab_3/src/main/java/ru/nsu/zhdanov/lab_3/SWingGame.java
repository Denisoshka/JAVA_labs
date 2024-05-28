package ru.nsu.zhdanov.lab_3;

import ru.nsu.zhdanov.lab_3.swing_facade.MainController;

import javax.swing.*;

public class SWingGame {
  private static MainController controller;

  public static void main(String[] args) {
    controller = new MainController();
    SwingUtilities.invokeLater(() -> controller.performMenu());
  }
}
