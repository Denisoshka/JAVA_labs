package ru.nsu.zhdanov.lab_3.model.game_context;

public interface IOProcessing {
  void handleInput();

  void handleOutput() throws InterruptedException;

  void signalGameEnd();
}
