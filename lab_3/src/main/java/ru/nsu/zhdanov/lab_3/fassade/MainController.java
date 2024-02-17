package ru.nsu.zhdanov.lab_3.fassade;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import lombok.Getter;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.PlayerAction;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.game_context.entity.SpriteSupplier;
import ru.nsu.zhdanov.lab_3.properties_loader.PropertiesLoader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController {
  //todo maybe perenesti into hello Application?
  protected GameEngine context;
  protected @Getter Map<KeyCode, AtomicBoolean> keysInput;//
  protected @Getter Map<MouseButton, AtomicBoolean> mouseInput;//
  protected ArrayList<Double> mouseCords;//
  @FXML
  protected Canvas gameCanvas;//
  protected GraphicsContext graphicsContext;//
  protected AnimationTimer gameLoop;//
  protected Map<ContextID, Image> sprites;//

  @FXML
  private void initialize() {
    initInput("key_input.properties", "mouse_input.properties");
    downloadRequiredImages("sprite.properties");

    this.graphicsContext = gameCanvas.getGraphicsContext2D();
    this.context = new GameEngine(null);
//    todo что я хотел здесь сделать
    this.gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        getUserInput();
        context.update();
        draw();
      }
    };
    this.gameLoop.start();
  }

  private void getUserInput() {
    double x = mouseCords.getFirst() - gameCanvas.getWidth() / 2;
    double y = mouseCords.getLast() - gameCanvas.getHeight() / 2;
    double angle = Math.atan2(x, y);
    context.setCameraCos(Math.cos(angle));
    context.setCameraCos(Math.sin(angle));
  }

  private void initKeyboard() {
    gameCanvas.setOnKeyPressed(event -> {
      if (keysInput.containsKey(event.getCode())) {
        keysInput.get(event.getCode()).set(true);
      }
    });

    gameCanvas.setOnKeyReleased(event -> {
      if (keysInput.containsKey(event.getCode())) {
        keysInput.get(event.getCode()).set(false);
      }
    });
  }

  private void initMouse() {
    gameCanvas.setOnMouseMoved(mouseEvent -> {
      mouseCords.set(1, mouseEvent.getSceneX());
      mouseCords.set(2, mouseEvent.getSceneY());
    });

    gameCanvas.setOnMousePressed(mouseEvent -> {
      if (mouseInput.containsKey(mouseEvent.getButton())) {
        mouseInput.get(mouseEvent.getButton()).set(true);
      }
    });

    gameCanvas.setOnMouseReleased(mouseEvent -> {
      if (mouseInput.containsKey(mouseEvent.getButton())) {
        mouseInput.get(mouseEvent.getButton()).set(false);
      }
    });
  }

  private void initInput(final String keyProp, final String mouseProp) {
    Properties keyProperties = PropertiesLoader.load(keyProp);
    Properties mouseProperties = PropertiesLoader.load(mouseProp);

    for (PlayerAction key : PlayerAction.values()) {
      String keyName = keyProperties.getProperty(String.valueOf(key));
      if (keyName != null) {
        KeyCode code = KeyCode.valueOf(keyName);
        AtomicBoolean indicator = new AtomicBoolean(false);
        keysInput.put(code, indicator);
        context.getInput().put(key, indicator);
      }
      String buttonName = mouseProperties.getProperty(String.valueOf(key));
      if (buttonName != null) {
        MouseButton code = MouseButton.valueOf(buttonName);
        AtomicBoolean indicator = new AtomicBoolean(false);
        mouseInput.put(code, indicator);
        context.getInput().put(key, indicator);
      }
    }

    initKeyboard();
    initMouse();
  }

  private void downloadRequiredImages(final String name) {
    Properties properties = PropertiesLoader.load(name);
    for (ContextID image : ContextID.values()) {
      Image sprite = new Image(properties.getProperty(image.name()));
      sprites.put(image, sprite);
    }
  }

  private void draw() {
    graphicsContext.drawImage(sprites.get(ContextID.Map), 0, 0);
    for (Entity tmp : context.getDrawInf()) {
      graphicsContext.drawImage(sprites.get(tmp.getSprite()), tmp.getX(), tmp.getY(), 10, 10);
    }
  }
}
