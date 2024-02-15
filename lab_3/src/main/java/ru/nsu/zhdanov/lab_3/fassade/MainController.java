package ru.nsu.zhdanov.lab_3.fassade;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;


import java.util.Map;

public class MainController {
  //todo maybe perenesti into hello Application?
  private GameEngine engine;
  protected @Getter Map<KeyCode, Boolean> keyInput;
  protected @Getter Map<String, Double> mouseInput;
  private Map<KeyCode, Boolean> pressedKeys;
  Scene gameScreen;

  @FXML
  private void initialize() {
    MainController.class.getClassLoader();
    gameScreen.setOnKeyPressed(event -> {
      if (keyInput.containsKey(event.getCode())) {
        keyInput.compute(event.getCode(), (key, currentValue) -> true);
      }
    });
  }

  private void getUserInput() {


    keyInput.replaceAll((key, oldValue) -> false);
  }

  private void perform() {
    while (true) {
      getUserInput();
      engine.update();

    }
  }

}
