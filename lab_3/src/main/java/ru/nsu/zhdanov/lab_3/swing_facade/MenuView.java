package ru.nsu.zhdanov.lab_3.swing_facade;

import com.fasterxml.jackson.databind.JsonNode;
import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.model.main_model.MainModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuView extends JFrame {
  private static final String START_GAME_LABEL = "start game";
  private static final String EXIT_GAME_LABEL = "exit game";
  private static final Object[] COLUMNS_LABELS = {"Name", "Score"};

  private JButton start;
  private JButton exit;
  private JTextField playerName;
  private MenuController controller;
  private JFrame view;
  private JTable scoresTable;

  MenuView(MainController menuContext) {
    controller = new MenuController(menuContext);
    this.initButtons();
    this.setVisible(true);
  }

  private void initButtons() {
    this.start = new JButton(START_GAME_LABEL);
    this.start.addActionListener(e -> MenuView.this.controller.startGame());
    this.exit = new JButton(EXIT_GAME_LABEL);
    this.exit.addActionListener(e -> {
      MenuView.this.controller.shutdown();
      MenuView.super.dispose();
    });
  }

  private void initScoreTable() {
    this.scoresTable = new JTable(controller.acquireScore(), COLUMNS_LABELS);
  }
}