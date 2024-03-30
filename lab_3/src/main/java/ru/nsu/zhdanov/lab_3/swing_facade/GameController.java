package ru.nsu.zhdanov.lab_3.swing_facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import ru.nsu.zhdanov.lab_3.abstract_facade.MainControllerRequests;
import ru.nsu.zhdanov.lab_3.abstract_facade.SubControllerRequests;
import ru.nsu.zhdanov.lab_3.swing_facade.MainController;
import ru.nsu.zhdanov.lab_3.model.game_context.GameSession;
import ru.nsu.zhdanov.lab_3.model.game_context.IOProcessing;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.Entity;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.ContextID;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.context_labels.PlayerAction;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.player.PlayerController;
import ru.nsu.zhdanov.lab_3.model.game_context.entity.wearpon.base_weapons.Weapon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class GameController implements SubControllerRequests {


  final private @Getter Map<Integer, AtomicBoolean> keysInput = new HashMap<>();//
  final private @Getter Map<Integer, AtomicBoolean> mouseInput = new HashMap<>();//
  final private AtomicIntegerArray mouseCords = new AtomicIntegerArray(2);//
  private GameSession context;


  private Graphics2D graphicsContext;

  private final Map<ContextID, SpriteInf> toDrawSprites = new HashMap<>();
  private final AtomicBoolean sceneDrawn = new AtomicBoolean(false);

  //  private Stage primaryStage;
  private MainControllerRequests.GameContext mainController;
  private final AtomicBoolean scoreDumped = new AtomicBoolean(false);

  private final IOProcessing IOHandler = new IOProcessing() {
    @Override
    public void handleInput() {
      if (keysInput.get(KeyEvent.VK_CLOSE_BRACKET).get()) {
        context.shutdown();
      }
      context.setCursorXPos(mouseCords.get(0));
      context.setCursorYPos(mouseCords.get(1));
    }

    @Override
    public void handleOutput() throws InterruptedException {
      SwingUtilities.invokeLater(GameController.this::draw);
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
      CountDownLatch latch = new CountDownLatch(1);
      try (ExecutorService endHandler = Executors.newFixedThreadPool(workers)) {
        SwingUtilities.invokeLater(() -> {
          graphicsContext.drawString(
                  "Game end with score {" + context.getScore() + "}",
                  (float) (context.getMap().getMaxX() / 4),
                  (float) (context.getMap().getMaxY() / 2)
          );
        });

        endHandler.submit(() -> {
          mainController.dumpScore(context.getPlayerName(), context.getScore());
          SwingUtilities.invokeLater(() -> {
            graphicsContext.drawString("Press space to exit",
                    (float) (context.getMap().getMaxX() / 4),
                    (float) (context.getMap().getMaxY() / 1.5)
            );
          });
          latch.countDown();
        });

        endHandler.submit(() -> {
          try {
            while (!keysInput.get(KeyEvent.VK_SPACE).get() || !scoreDumped.get()) {
              Thread.sleep(1000 / 30);
            }
            latch.await();
          } catch (InterruptedException e) {
            return;
          }

          SwingUtilities.invokeLater(() -> {
            mainController.gameEnd();
          });
        });
      }
    }
  };

  private void draw() {
    drawMap();
    drawEntities();
    drawPlayer();
//    drawBar();
    synchronized (sceneDrawn) {
      sceneDrawn.set(true);
      sceneDrawn.notifyAll();
    }
  }

  public GameController(Properties properties, GameView view, MainController mainController) {
    this.mainController = mainController;
    this.context = new GameSession(properties, IOHandler, mainController.getPlayerName());

    Properties keyProperties = new Properties();
    Properties mouseProperties = new Properties();
    Properties spriteProperties = new Properties();
    try {
      keyProperties.load(Objects.requireNonNull(getClass().getResourceAsStream(properties.getProperty("keyInput"))));
      mouseProperties.load(Objects.requireNonNull(getClass().getResourceAsStream(properties.getProperty("mouseInput"))));
      spriteProperties.load(Objects.requireNonNull(getClass().getResourceAsStream(properties.getProperty("spriteInf"))));
    } catch (IOException | NullPointerException e) {
      throw new RuntimeException("unable to load facade resource");
    }

    initInput(keyProperties, mouseProperties);
    initSprites(spriteProperties);
  }

  @Override
  public void perform() {
    context.perform();
  }

  @Override
  public void shutdown() {
    context.shutdown();
  }

  //todo need to refactor
  /*private void drawBar() {
    PlayerController pl = context.getPlayer();
    curScore.setText("Score: " + context.getScore());
    livesQuantity.setText("Lives: " + pl.getLivesQuantity());

    if (pl.getWeapon() != null) {
      weaponName.setText(pl.getWeapon().getID().name());
      weaponCondition.setText("Ammunition: " + context.getWeaponOccupancy() + " / " + context.getWeaponCapacity());
    }
  }*/

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
    graphicsContext.drawImage(inf.image(), x + inf.shiftX, y + inf.shiftY(), inf.width(), inf.height(), null);

  }

  public void handleReleasedKey(KeyEvent keyEvent) {
    AtomicBoolean rez = keysInput.get(keyEvent.getKeyCode());
    if (rez != null) {
      rez.set(false);
    }
  }

  public void handlePressedKey(KeyEvent keyEvent) {
    AtomicBoolean rez = keysInput.get(keyEvent.getKeyCode());
    if (rez != null) {
      rez.set(true);
    }
  }

  public void handleMouseTrack(MouseEvent mouseEvent) {
    mouseCords.set(0, mouseEvent.getX());
    mouseCords.set(1, mouseEvent.getY());
  }

  public void handleMousePressed(MouseEvent mouseEvent) {
    AtomicBoolean rez = mouseInput.get(mouseEvent.getButton());
    if (rez != null) {
      rez.set(true);
    }
  }

  public void handleMouseReleased(MouseEvent mouseEvent) {
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
      for (ContextID id : ContextID.values()) {
        Image sprite = Toolkit.getDefaultToolkit().getImage(
                Objects.requireNonNull(getClass().getResource(prop.getProperty(id.name())))
        );
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
      throw new RuntimeException("Unable to download image");
    }
  }

  private void initInput(final Properties keyProp, final Properties mouseProp) {
    for (PlayerAction key : PlayerAction.values()) {
      String keyName = keyProp.getProperty(String.valueOf(key));
      if (keyName != null) {
        bindInput(keysInput, context.getInputTrace(), KeyButtons.valueOf(keyName).getCode(), key);
      }

      String buttonName = mouseProp.getProperty(String.valueOf(key));
      if (buttonName != null) {
        bindInput(mouseInput, context.getInputTrace(), MouseButtons.valueOf(buttonName).getCode(), key);
      }
    }

    keysInput.put(KeyEvent.VK_CLOSE_BRACKET, new AtomicBoolean(false));
    keysInput.put(KeyEvent.VK_SPACE, new AtomicBoolean(false));
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

  private enum KeyButtons {
    W(KeyEvent.VK_W),
    SHIFT(KeyEvent.VK_SHIFT),
    A(KeyEvent.VK_A),
    D(KeyEvent.VK_D),
    S(KeyEvent.VK_S),
    R(KeyEvent.VK_R),
    DIGIT1(KeyEvent.VK_1),
    DIGIT2(KeyEvent.VK_2);

    final int code;

    private KeyButtons(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }

  private enum MouseButtons {
    PRIMATY(MouseEvent.BUTTON1);

    final int code;

    private MouseButtons(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }
}
