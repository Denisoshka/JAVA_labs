package ru.nsu.zhdanov.lab_3.fassade;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.properties_loader.PropertiesLoader;


import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {
//  enum Mouse

  //todo maybe perenesti into hello Application?
  protected GameEngine engine;
  protected @Getter Map<KeyCode, AtomicBoolean> keysInput;
  protected @Getter Map<MouseButton, AtomicBoolean> mouseInput;
  protected ArrayList<Double> mouseCords;
  protected Map<KeyCode, GameEngine.EngineInput> keyInputAssoc;
  protected Map<MouseButton, GameEngine.EngineInput> mouseInputAssoc;

  protected Map<KeyCode, Boolean> pressedKeys;
  Scene gameScreen;

  @FXML
  private void initialize() {
//    MainController.class.getClassLoader();


  }

  private void getUserInput() {

  }

  private void perform() {
    while (true) {
      getUserInput();
      engine.update();

    }
  }


  private void initKeyboard() {
    gameScreen.setOnKeyPressed(event -> {
      if (keysInput.containsKey(event.getCode())) {
        keysInput.get(event.getCode()).set(true);
      }
    });
    gameScreen.setOnKeyReleased(event -> {
      if (keysInput.containsKey(event.getCode())) {
        keysInput.get(event.getCode()).set(false);
      }
    });
  }

  private void initMouse() {
    gameScreen.setOnMouseMoved(mouseEvent -> {
      mouseCords.set(1, mouseEvent.getSceneX());
      mouseCords.set(2, mouseEvent.getSceneY());
    });
    gameScreen.setOnMousePressed(mouseEvent -> {
      if (mouseInput.containsKey(mouseEvent.getButton())) {
        mouseInput.get(mouseEvent.getButton()).set(true);
      }
    });
    gameScreen.setOnMouseReleased(mouseEvent -> {
      if (mouseInput.containsKey(mouseEvent.getButton())) {
        mouseInput.get(mouseEvent.getButton()).set(false);
      }
    });
  }

  private void initInput(final String keyProp, final String mouseProp) {
    Properties keyProperties = PropertiesLoader.load(keyProp);
    Properties mouseProperties = PropertiesLoader.load(mouseProp);

    for (GameEngine.EngineInput key : GameEngine.EngineInput.values()) {
      String keyName = keyProperties.getProperty(String.valueOf(key));
      if (keyName != null) {
        KeyCode code = KeyCode.valueOf(keyName);
        AtomicBoolean indicator = new AtomicBoolean(false);
        keysInput.put(code, indicator);
        engine.getInput().put(key, indicator);
      }
      String buttonName = mouseProperties.getProperty(String.valueOf(key));
      if (buttonName != null) {
        MouseButton code = MouseButton.valueOf(buttonName);
        AtomicBoolean indicator = new AtomicBoolean(false);
        mouseInput.put(code, indicator);
        engine.getInput().put(key, indicator);
      }
    }

    initKeyboard();
    initMouse();
  }
}
