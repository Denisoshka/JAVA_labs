package ru.nsu.zhdanov.lab_3.swing_facade;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JFrame implements BaseViewInterface {
  private static final String START_GAME_LABEL = "start game";
  private static final String EXIT_GAME_LABEL = "exit game";
  private static final Object[] COLUMNS_LABELS = {"Name", "Score"};

  private JTextField playerName;
  private MenuController controller;
  private JTable scoresTable;
  private JButton startGameButton;
  private JButton exitButton;

  public MenuView(MainController menuContext) {
    this.setLocationRelativeTo(null);
    this.setSize(600, 400);
    setLayout(new BorderLayout());
    this.controller = new MenuController(menuContext);
    this.initButtons();
    this.initScoreTable();
  }

  private void initButtons() {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.startGameButton = new JButton(START_GAME_LABEL);
    this.exitButton = new JButton(EXIT_GAME_LABEL);
    this.playerName = new JTextField("default player");
    buttonPanel.add(playerName);
    buttonPanel.add(startGameButton);
    buttonPanel.add(exitButton);
    add(buttonPanel, BorderLayout.WEST);

    this.startGameButton.addActionListener(e -> MenuView.this.controller.startGame());
    this.exitButton.addActionListener(e -> {
      MenuView.this.controller.shutdown();
      MenuView.super.dispose();
    });
  }

  private void initScoreTable() {
    this.scoresTable = new JTable(controller.acquireScore(), COLUMNS_LABELS);
    JScrollPane tableScrollPane = new JScrollPane(scoresTable);
    this.add(tableScrollPane, BorderLayout.CENTER);
    scoresTable.setFillsViewportHeight(true);
  }

  @Override
  public void perform() {
    this.setVisible(true);
  }

  @Override
  public void shutdown() {
    this.dispose();
  }
}