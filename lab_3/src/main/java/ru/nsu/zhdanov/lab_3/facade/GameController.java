package ru.nsu.zhdanov.lab_3.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.zhdanov.lab_3.model.game_context.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameEngine;
import ru.nsu.zhdanov.lab_3.model.game_context.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.Player;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.Weapon;

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

  @FXML
  public TextField weaponCondition;
  @FXML
  public TextField livesQuantity;
  @FXML
  public TextField curScore;
  @FXML
  private TextField weaponName;

  final private @Getter Map<KeyCode, AtomicBoolean> keysInput = new HashMap<>();//
  final private @Getter Map<MouseButton, AtomicBoolean> mouseInput = new HashMap<>();//
  final private AtomicIntegerArray mouseCords = new AtomicIntegerArray(2);//
  private GameEngine context;

  @FXML
  private Canvas canvas;
  private GraphicsContext graphicsContext;

  final private Map<ContextID, SpriteInf> toDrawSprites = new HashMap<>();
  final AtomicBoolean sceneDrawn = new AtomicBoolean(false);
  final AtomicBoolean continueGame = new AtomicBoolean(true);

  private Stage primaryStage;
  private MainControllerRequests.GameContext mainController;

  private final Thread gameThread = new Thread(() -> {
    long start, end, diff;
    context.perform();

    while (!context.gameIsEnd() && continueGame.get()) {
      start = System.currentTimeMillis();

      processInput();
      context.update();
      Platform.runLater(this::draw);

      try {
        synchronized (sceneDrawn) {
          while (!sceneDrawn.get()) {
            sceneDrawn.wait();
          }
        }
      } catch (InterruptedException e) {
        return;
      }

      sceneDrawn.set(false);
      end = System.currentTimeMillis();

      if ((diff = end - start) < timeToUpdate) {
        try {
          Thread.sleep(timeToUpdate - (diff));
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }

    exit();
  });

  private void exit() {
    Font font = new Font("Arial", 20);
    Platform.runLater(() -> {
      graphicsContext.setFont(font);
      graphicsContext.fillText(
              "Press space to exit",
              (double) context.getMap().getMaxX() / 2,
              (double) context.getMap().getMaxY() / 2
      );
    });
    while (!keysInput.get(KeyCode.SPACE).get()) {
    }
    mainController.dumpScore(context.getPlayerName(), context.getPlayerScore());
    Platform.runLater(() -> {
      primaryStage.setFullScreen(false);
      mainController.gameEnd();
    });

  }

  @Override
  public void setContext(Properties properties, MainController controller, Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.mainController = controller;
    Platform.runLater(() -> {
      this.primaryStage.setFullScreen(true);
    });
    this.context = new GameEngine(properties, mainController.getPlayerName());

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
    gameThread.start();
  }

  @Override
  public void shutdown() {
    gameThread.interrupt();
  }

  private void processInput() {
    if (keysInput.get(KeyCode.CLOSE_BRACKET).get()) {
      continueGame.set(false);
    }

    int xPos = mouseCords.get(0);
    int yPos = mouseCords.get(1);
    context.setCursorXPos(xPos);
    context.setCursorYPos(yPos);
  }

  private void allowGoToNextStep() {
    synchronized (sceneDrawn) {
      sceneDrawn.set(true);
      sceneDrawn.notifyAll();
    }
  }

  private void draw() {
    drawContext();
    drawCondition();
    allowGoToNextStep();
  }

  private void drawCondition() {
    Player pl = context.getPlayer();
    curScore.setText("Score: " + context.getPlayerScore());
    livesQuantity.setText("Lives: " + pl.getLivesQuantity());
    if (pl.getWeapon() != null) {
      weaponName.setText(pl.getWeapon().getID().name());
      weaponCondition.setText("Active: " + pl.getWeapon().readyForUse());
    } else {
      weaponName.setText("No weapon");
      weaponCondition.setText("");
    }
  }

  private void drawPlayer() {
    Player player = context.getPlayer();
    draw(toDrawSprites.get(player.getID()), player.getX(), player.getY());

    Weapon w = player.getWeapon();
    if (w != null) {
      draw(toDrawSprites.get(w.getID()), player.getX(), player.getY());
    }
  }

  private void drawMap() {
    draw(toDrawSprites.get(ContextID.Map), 0, 0);
  }

  private void drawEntities() {
    for (Entity ent : context.getEntities()) {
      draw(toDrawSprites.get(ent.getID()), ent.getX(), ent.getY());
    }
  }

  private void draw(SpriteInf inf, int x, int y) {
    graphicsContext.drawImage(inf.image(), x + inf.shiftX, y + inf.shiftY(), inf.width(), inf.height());
  }

  private void drawContext() {
    drawMap();
    drawEntities();
    drawPlayer();
  }

  @FXML
  private void handleReleasedKey(KeyEvent keyEvent) {
    AtomicBoolean rez = keysInput.get(keyEvent.getCode());
    if (rez != null) {
      rez.set(false);
    }
  }

  @FXML
  private void handlePressedKey(KeyEvent keyEvent) {
    AtomicBoolean rez = keysInput.get(keyEvent.getCode());
    if (rez != null) {
      rez.set(true);
    }
  }

  @FXML
  private void handleMouseTrack(MouseEvent mouseEvent) {
    mouseCords.set(0, (int) mouseEvent.getX());
    mouseCords.set(1, (int) mouseEvent.getY());
  }

  @FXML
  private void handleMousePressed(MouseEvent mouseEvent) {
    AtomicBoolean rez = mouseInput.get(mouseEvent.getButton());
    if (rez != null) {
      rez.set(true);
    }
  }

  @FXML
  private void trackMouseReleased(MouseEvent mouseEvent) {
    AtomicBoolean rez = mouseInput.get(mouseEvent.getButton());
    if (rez != null) {
      rez.set(false);
    }
  }

  //  todo make exception
  private void initSprites(final Properties prop) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode tree;

    try {
      tree = mapper.readTree(getClass().getResourceAsStream(prop.getProperty("SpriteInf")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
//      todo сделать парс жсона в модели?
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
    for (PlayerAction key : PlayerAction.values()) {
      String keyName = keyProp.getProperty(String.valueOf(key));
      if (keyName != null) {
        bindInput(keysInput, context.getInput(), KeyCode.valueOf(keyName), key);
      }

      String buttonName = mouseProp.getProperty(String.valueOf(key));
      if (buttonName != null) {
        bindInput(mouseInput, context.getInput(), MouseButton.valueOf(buttonName), key);
      }
    }

    keysInput.put(KeyCode.CLOSE_BRACKET, new AtomicBoolean(false));
    keysInput.put(KeyCode.SPACE, new AtomicBoolean(false));
  }

  private <KeyT> void bindInput(Map<KeyT, AtomicBoolean> from,
                                Map<PlayerAction, AtomicBoolean> to,
                                KeyT keyFrom, PlayerAction keyTo) {
    AtomicBoolean indicator = new AtomicBoolean(false);
    from.put(keyFrom, indicator);
    to.put(keyTo, indicator);
  }

  private record SpriteInf(Image image, int shiftX, int shiftY, int width, int height) {
  }
}
