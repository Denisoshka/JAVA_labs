package ru.nsu.zhdanov.lab_3.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.ContextID;
import ru.nsu.zhdanov.lab_3.model.GameEngine;
import ru.nsu.zhdanov.lab_3.model.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.entity.player.Player;
import ru.nsu.zhdanov.lab_3.properties_loader.PropertiesLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Slf4j
public class GameController implements SubControllerRequests {
  private final static long timeToUpdate = 17;

  private @Getter Map<KeyCode, AtomicBoolean> keysInput;//
  private @Getter Map<MouseButton, AtomicBoolean> mouseInput;//
  private AtomicIntegerArray mouseCords;//
  private GameEngine context;

  @FXML
  private Canvas canvas;//
  private GraphicsContext graphicsContext;

  private Map<ContextID, SpriteInf> toDrawSprites;

  private int gcXShift = 0;
  private int gcYShift = 0;

  private long lastUpdateTime = 0;
  private int targetFPS = 60;

  final AtomicBoolean sceneDrawn = new AtomicBoolean(false);

  private final Thread gameThread = new Thread(() -> {
    long start, end, diff;
    while (true) {
      start = System.currentTimeMillis();
      getUserInput();
      context.update();
      Platform.runLater(this::draw);
      Platform.runLater(this::drawHitBox);
      try {
        synchronized (sceneDrawn) {
          while (!sceneDrawn.get()) {
            sceneDrawn.wait();
          }
        }
      } catch (InterruptedException e) {
//        log.info("thread pizda");
        return;
      }
//      log.info("draw set false");
      sceneDrawn.set(false);
      end = System.currentTimeMillis();
//      if (end - start < timeToUpdate ){
//      }
      if ((diff = end - start) < timeToUpdate) {
        try {
          Thread.sleep(timeToUpdate - (diff));
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }


      }
    }
  });

  @Override
  public void setContext(Properties properties, MainController controller) {
    log.info("set game context");
    context = new GameEngine(null);
    Properties keyProperties = new Properties();
    Properties mouseProperties = new Properties();
    Properties spriteProperties = new Properties();

    try {
      keyProperties.load(getClass().getResourceAsStream(properties.getProperty("keyInput")));
      mouseProperties.load(getClass().getResourceAsStream(properties.getProperty("mouseInput")));
      spriteProperties.load(getClass().getResourceAsStream(properties.getProperty("spriteInf")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    initInput(keyProperties, mouseProperties);
    initSprites(spriteProperties);
  }

  @FXML
  private void initialize() {
    graphicsContext = canvas.getGraphicsContext2D();
    canvas.requestFocus();
  }

  @Override
  public void perform() {
    context.perform();
    gameThread.start();
  }

  @Override
  public void shutdown() {
    gameThread.interrupt();
  }

  private void getUserInput() {
    int xPos = mouseCords.get(0) - gcXShift;
    int yPos = mouseCords.get(1) - gcYShift;
    context.setCursorXPos(xPos);
    context.setCursorYPos(yPos);
  }

  private void drawHitBox() {
    for (Entity ent : context.getEntities()) {
      graphicsContext.fillOval(ent.getX(), ent.getY(), ent.getRadius() * 2, ent.getRadius() * 2);
    }

    Player player = context.getPlayer();
    graphicsContext.fillOval(player.getX(), player.getY(), player.getRadius() * 2, player.getRadius() * 2);
    synchronized (sceneDrawn) {
      sceneDrawn.set(true);
      sceneDrawn.notifyAll();
    }
  }

  private void draw() {
    log.info("draw context");
    SpriteInf entInf = toDrawSprites.get(ContextID.Map);
    graphicsContext.drawImage(
            entInf.image,
            entInf.shiftX,
            entInf.shiftY,
            entInf.width,
            entInf.height
    );

    for (Entity ent : context.getEntities()) {
      entInf = toDrawSprites.get(ent.getID());
      graphicsContext.drawImage(
              entInf.image,
              ent.getX() + entInf.shiftX,
              ent.getY() + entInf.shiftY,
              entInf.width,
              entInf.height
      );
    }

    Player player = context.getPlayer();
    entInf = toDrawSprites.get(player.getID());
    graphicsContext.drawImage(
            entInf.image,
            player.getX() + entInf.shiftX,
            player.getY() + entInf.shiftY,
            entInf.width,
            entInf.height
    );


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
  private void handleMousePressed(MouseEvent mouseEvent) {
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

  //  todo make exception
  private void initSprites(final Properties prop) {
    toDrawSprites = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode tree;

    try {
      tree = mapper.readTree(getClass().getResourceAsStream(prop.getProperty("SpriteInf")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
//      todo make load of def texture
      for (ContextID id : ContextID.values()) {
        Image sprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream(prop.getProperty(id.name()))));
        JsonNode curNode = tree.get(id.name());
        toDrawSprites.put(id, new SpriteInf(
                sprite,
                curNode.get("shiftX").asInt(),
                curNode.get("shiftY").asInt(),
                curNode.get("width").asInt(),
                curNode.get("height").asInt()
        ));
      }
    } catch (NullPointerException e) {
      throw new RuntimeException(e);
    }
  }

  private void initInput(final Properties keyProp, final Properties mouseProp) {
    keysInput = new HashMap<>();
    mouseInput = new HashMap<>();
    mouseCords = new AtomicIntegerArray(2);

    for (PlayerAction key : PlayerAction.values()) {
      String keyName = keyProp.getProperty(String.valueOf(key));
      if (keyName != null) {
        KeyCode code = KeyCode.valueOf(keyName);
        AtomicBoolean indicator = new AtomicBoolean(false);
        keysInput.put(code, indicator);
        context.getInput().put(key, indicator);
      }
      String buttonName = mouseProp.getProperty(String.valueOf(key));
      if (buttonName != null) {
        MouseButton code = MouseButton.valueOf(buttonName);
        AtomicBoolean indicator = new AtomicBoolean(false);
        mouseInput.put(code, indicator);
        context.getInput().put(key, indicator);
      }
    }
  }


  private record SpriteInf(Image image, int shiftX, int shiftY, int width, int height) {
  }
}
