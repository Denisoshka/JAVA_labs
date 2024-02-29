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
import ru.nsu.zhdanov.lab_3.game_context.interfaces.DrawInterface;
import ru.nsu.zhdanov.lab_3.properties_loader.PropertiesLoader;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Slf4j
public class MainController {
  //todo maybe perenesti into hello Application?
  private GameEngine context;
  private @Getter Map<KeyCode, AtomicBoolean> keysInput;//
  private @Getter Map<MouseButton, AtomicBoolean> mouseInput;//
  private AtomicIntegerArray mouseCords;//
  @FXML
  private Canvas gameCanvas;//
  private GraphicsContext graphicsContext;//
  private int gcXShift = 0;
  private int gcYShift = 0;
  private long lastUpdateTime = 0;
  private int targetFPS = 60;


  private AnimationTimer gameLoop = new AnimationTimer() {
    @Override
    public void handle(long now) {
      long elapsedTime = now - lastUpdateTime;
      double secondsElapsed = (double) elapsedTime / 1_000_000_000.0;
      // Выполнение логики обновления с определенным FPS
      if (secondsElapsed > 1.0 / targetFPS) {
        getUserInput();
        context.update();

        context.drawScene(view);
        lastUpdateTime = now;
      }
    }
  };//
  private Map<ContextID, Image> sprites;//
  DrawInterface view;

  @FXML
  private void initialize() {
//    todo чя хотел здесь сделать
    view = new View();
    context = new GameEngine(null);
    initInput("key_input.properties", "mouse_input.properties");
    downloadRequiredImages("sprite.properties");
    graphicsContext = gameCanvas.getGraphicsContext2D();
    gameCanvas.requestFocus();
    gameLoop.start();
  }


  private void getUserInput() {
    int xPos = mouseCords.get(0) - gcXShift;
    int yPos = mouseCords.get(1) - gcYShift;
    context.setCursorXPos(xPos);
    context.setCursorYPos(yPos);
  }

  @FXML
  private void handleReleasedKey(KeyEvent keyEvent) {
    if (keysInput.containsKey(keyEvent.getCode())) {
//      log.info(keyEvent.getCode().toString() + " released");
      keysInput.get(keyEvent.getCode()).set(false);
    }
  }

  @FXML
  private void handlePressedKey(KeyEvent keyEvent) {
    if (keysInput.containsKey(keyEvent.getCode())) {
//      log.info(keyEvent.getCode().toString() + " pressed");
      keysInput.get(keyEvent.getCode()).set(true);
    }
  }

  @FXML
  private void handleMouseTrack(MouseEvent mouseEvent) {
    mouseCords.set(0, (int) mouseEvent.getSceneX());
    mouseCords.set(1, (int) mouseEvent.getSceneY());
//    log.info("cur mouse cords x=" + mouseCords.get(0) + " y=" +  mouseCords.get(1));
  }

  @FXML
  private void trackMousePressed(MouseEvent mouseEvent) {
    if (mouseInput.containsKey(mouseEvent.getButton())) {
//      log.info("pressed " + mouseEvent.getButton().toString());
      mouseInput.get(mouseEvent.getButton()).set(true);
    }
  }

  @FXML
  private void trackMouseReleased(MouseEvent mouseEvent) {
    if (mouseInput.containsKey(mouseEvent.getButton())) {
//      log.info("released " + mouseEvent.getButton().toString());
      mouseInput.get(mouseEvent.getButton()).set(false);
    }
  }

  private void initInput(final String keyProp, final String mouseProp) {
    Properties keyProperties = PropertiesLoader.load(keyProp);
    Properties mouseProperties = PropertiesLoader.load(mouseProp);
    keysInput = new HashMap<>();
    mouseInput = new HashMap<>();
    mouseCords = new AtomicIntegerArray(2);

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


  //  todo make exception
  private void downloadRequiredImages(final String name) {
    sprites = new HashMap<>();
    Properties properties = PropertiesLoader.load(name);
    for (ContextID image : ContextID.values()) {
      Image sprite = new Image(Objects.requireNonNull(getClass()
              .getResourceAsStream(properties.getProperty(image.name()))));
      sprites.put(image, sprite);
    }
  }


  private class View implements DrawInterface {
    @Override
    public void draw(ContextID id, int x0, int y0, int width, int height, double sin, double cos, boolean reflect) {
      graphicsContext.drawImage(sprites.get(id), x0, y0, width, height);
    }
  }
}
