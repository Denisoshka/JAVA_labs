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
  protected @Getter Map<KeyCode, Boolean> keysInput;
  protected @Getter Map<String, Double> mouseInput;
  private Map<KeyCode, Boolean> pressedKeys;
  Scene gameScreen;

  @FXML
  private void initialize() {
//    MainController.class.getClassLoader();


  }

  private void getUserInput() {


    keysInput.replaceAll((key, oldValue) -> false);
  }

  private void perform() {
    while (true) {
      getUserInput();
      engine.update();

    }
  }


  private void initKeyboard() {
    gameScreen.setOnKeyPressed(event -> {
      keysInput.replace(event.getCode(), true);
    });
    gameScreen.setOnKeyReleased(event -> {
      keysInput.replace(event.getCode(), false);
    });
  }

  private void initMouse() {

    gameScreen.setOnMpressed(event -> {
      if (keysInput.containsKey(event.getCode())) {
        keysInput.compute(event.getCode(), (key, currentValue) -> true);
      }
    });
  }

}
