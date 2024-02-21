package ru.nsu.zhdanov.lab_3.facade;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.game_context.PlayerAction;
import ru.nsu.zhdanov.lab_3.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.properties_loader.PropertiesLoader;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class MainController {
  //todo maybe perenesti into hello Application?
  protected GameEngine context;
  protected @Getter Map<KeyCode, AtomicBoolean> keysInput;//
  protected @Getter Map<MouseButton, AtomicBoolean> mouseInput;//
  protected double[] mouseCords;//
  @FXML
  protected Canvas gameCanvas;//
  protected GraphicsContext graphicsContext;//
  protected AnimationTimer gameLoop;//
  protected Map<ContextID, Image> sprites;//

  @FXML
  private void initialize() {
    this.context = new GameEngine(null);
    initInput("key_input.properties", "mouse_input.properties");
    downloadRequiredImages("sprite.properties");

    this.graphicsContext = gameCanvas.getGraphicsContext2D();
//    todo что я хотел здесь сделать
    this.gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        getUserInput();
        context.update();
        draw();
      }
    };
    gameCanvas.requestFocus();
    this.gameLoop.start();
  }

  private void getUserInput() {
    double x = mouseCords[0] - gameCanvas.getWidth() / 2;
    double y = mouseCords[1] - gameCanvas.getHeight() / 2;
    double angle = Math.atan2(x, y);
    context.setCameraCos(Math.cos(angle));
    context.setCameraSin(Math.sin(angle));
  }

  @FXML
  private void handleReleasedKey(KeyEvent keyEvent) {
    if (keysInput.containsKey(keyEvent.getCode())) {
      log.info(keyEvent.getCode().toString() + " released");
      keysInput.get(keyEvent.getCode()).set(false);
    }
  }

  @FXML
  private void handlePressedKey(KeyEvent keyEvent) {
    if (keysInput.containsKey(keyEvent.getCode())) {
      log.info(keyEvent.getCode().toString() + " pressed");
      keysInput.get(keyEvent.getCode()).set(true);
    }
  }

  @FXML
  private void handleMouseTrack(MouseEvent mouseEvent) {
    mouseCords[0] = mouseEvent.getSceneX();
    mouseCords[1] = mouseEvent.getSceneY();
    log.info("cur mouse cords x=" + mouseCords[0] + " y=" + mouseCords[1]);
  }

  @FXML
  private void trackMousePressed(MouseEvent mouseEvent) {
    if (mouseInput.containsKey(mouseEvent.getButton())) {
      log.info("pressed " + mouseEvent.getButton().toString());
      mouseInput.get(mouseEvent.getButton()).set(true);
    }
  }

  @FXML
  private void trackMouseReleased(MouseEvent mouseEvent) {
    if (mouseInput.containsKey(mouseEvent.getButton())) {
      log.info("released " + mouseEvent.getButton().toString());
      mouseInput.get(mouseEvent.getButton()).set(false);
    }
  }

  private void initInput(final String keyProp, final String mouseProp) {
    Properties keyProperties = PropertiesLoader.load(keyProp);
    Properties mouseProperties = PropertiesLoader.load(mouseProp);
    keysInput = new HashMap<>();
    mouseInput = new HashMap<>();
    mouseCords = new double[2];

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
  }

  private void downloadRequiredImages(final String name) {
    sprites = new HashMap<>();
    Properties properties = PropertiesLoader.load(name);
    for (ContextID image : ContextID.values()) {
      Image sprite = new Image(Objects.requireNonNull(getClass()
              .getResourceAsStream(properties.getProperty(image.name()))));
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
