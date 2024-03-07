package ru.nsu.zhdanov.lab_3.facade;

public interface MainControllerRequests {
  interface MenuContext{
    public abstract void startGame();
  }
  interface GameContext{
    public abstract void gameEnd();
  }
}
