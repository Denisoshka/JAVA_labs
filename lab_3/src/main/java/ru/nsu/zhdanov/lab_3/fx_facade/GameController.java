package ru.nsu.zhdanov.lab_3.fx_facade;

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
import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.abstract_facade.SubControllerRequests;
import ru.nsu.zhdanov.lab_3.fx_facade.exceptions.ResourceNotAvailable;
import ru.nsu.zhdanov.lab_3.model.game_context.IOProcessing;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.Weapon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Slf4j
public class GameController implements SubControllerRequests, FXControllerInterface {
  @FXML
  public TextField weaponCondition;
  @FXML
  public TextField livesQuantity;
  @FXML
  public TextField curScore;
  @FXML
  private TextField weaponName;

  private final @Getter Map<KeyCode, AtomicBoolean> keysInput = new HashMap<>();//
  private final @Getter Map<MouseButton, AtomicBoolean> mouseInput = new HashMap<>();//
  private final AtomicIntegerArray mouseCords = new AtomicIntegerArray(2);//
  private GameSession context;


  @FXML
  private Canvas canvas;
  private GraphicsContext graphicsContext;

  private final Map<ContextID, SpriteInf> toDrawSprites = new HashMap<>();

  private Stage primaryStage;
  private MainControllerRequests.GameContext mainController;
  private final AtomicBoolean scoreDumped = new AtomicBoolean(false);
  private final AtomicBoolean sceneDrawn = new AtomicBoolean(false);

  private final IOProcessing IOHandler = new IOProcessing() {
    @Override
    public void handleInput() {
      if (keysInput.get(KeyCode.CLOSE_BRACKET).get()) {
        Platform.runLater(() -> {
          mainController.shutdownGameScreen();
        });
      }
      context.setCursorXPos(mouseCords.get(0));
      context.setCursorYPos(mouseCords.get(1));
    }

    @Override
    public void handleOutput() throws InterruptedException {
      Platform.runLater(() -> {
        synchronized (sceneDrawn) {
          GameController.this.draw();
          sceneDrawn.set(true);
          sceneDrawn.notifyAll();
        }
      });
      synchronized (sceneDrawn) {
        while (!sceneDrawn.get()) {
          sceneDrawn.wait();
        }
      }
      sceneDrawn.set(false);
    }

    @Override
    public void signalGameEnd() {
      int workers = 2;
      try (ExecutorService endHandler = Executors.newFixedThreadPool(workers)) {
        Platform.runLater(() -> {
          Font font = new Font("Arial", 20);
          graphicsContext.setFont(font);
          graphicsContext.fillText(
                  "Game end with score {" + context.getScore() + "}",
                  (double) context.getMap().getMaxX() / 4,
                  (double) context.getMap().getMaxY() / 2
          );
        });
        endHandler.submit(() -> {
          log.info("dump score");
          mainController.dumpScore(context.getPlayerName(), context.getScore());
          scoreDumped.set(true);
          Platform.runLater(() -> {
            Font font = new Font("Arial", 20);
            graphicsContext.setFont(font);
            graphicsContext.fillText(
                    "Press space to exit",
                    (double) context.getMap().getMaxX() / 4,
                    (double) context.getMap().getMaxY() / 1.8
            );
          });
        });
        endHandler.submit(() -> {
          try {
            while (!keysInput.get(KeyCode.SPACE).get() || !scoreDumped.get()) {
              Thread.sleep(1000 / 30);
            }
          } catch (InterruptedException e) {
            return;
          }

          Platform.runLater(() -> {
            mainController.shutdownGameScreen();
          });
        });
      }
    }
  };

  private void draw() {
    drawMap();
    drawEntities();
    drawPlayer();
    drawBar();
  }

  public void setContext(Properties properties, MainController controller, Stage stage) {
    this.primaryStage = stage;
    this.mainController = controller;
    Platform.runLater(() -> this.primaryStage.setFullScreen(true));
    this.context = new GameSession(properties, IOHandler, mainController.getPlayerName());

    Properties keyProperties = new Properties();
    Properties mouseProperties = new Properties();
    Properties spriteProperties = new Properties();
    String resName, keyName = null;
    try {
      keyName = "keyInput";
      resName = properties.getProperty(keyName);
      keyProperties.load(getClass().getResourceAsStream(resName));
      keyName = "mouseInput";
      resName = properties.getProperty(keyName);
      mouseProperties.load(getClass().getResourceAsStream(resName));
      keyName = "spriteInf";
      resName = properties.getProperty(keyName);
      spriteProperties.load(getClass().getResourceAsStream(resName));
    } catch (IOException | NullPointerException e) {
      throw new ResourceNotAvailable(keyName, e);
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
  }

  @Override
  public void shutdown() {
    context.shutdown();
  }

  private void drawBar() {
    PlayerController pl = context.getPlayer();
    curScore.setText("Score: " + context.getScore());
    livesQuantity.setText("Lives: " + pl.getLivesQuantity());

    if (pl.getWeapon() != null) {
      weaponName.setText(pl.getWeapon().getID().name());
      weaponCondition.setText("Ammunition: " + context.getWeaponOccupancy() + " / " + context.getWeaponCapacity());
    }
  }

  private void drawPlayer() {
    PlayerController player = context.getPlayer();
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
      throw new ResourceNotAvailable("Unable to load sprites info", e);
    }
    String spriteName = null;
    try {
      for (ContextID id : ContextID.values()) {
        spriteName = id.name();
        Image sprite = new Image(Objects.requireNonNull(getClass().getResourceAsStream(prop.getProperty(spriteName))));
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
      throw new ResourceNotAvailable("Unable to load sprite of " + spriteName, e);
    }
  }

  private void initInput(final Properties keyProp, final Properties mouseProp) {
    for (PlayerAction key : PlayerAction.values()) {
      String keyName = keyProp.getProperty(String.valueOf(key));
      if (keyName != null) {
        bindInput(keysInput, context.getInputTrace(), KeyCode.valueOf(keyName), key);
      }

      String buttonName = mouseProp.getProperty(String.valueOf(key));
      if (buttonName != null) {
        bindInput(mouseInput, context.getInputTrace(), MouseButton.valueOf(buttonName), key);
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
