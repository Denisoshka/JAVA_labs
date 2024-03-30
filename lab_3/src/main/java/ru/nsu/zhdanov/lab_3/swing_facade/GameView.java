package ru.nsu.zhdanov.lab_3.swing_facade;

import ru.nsu.zhdanov.lab_3.model.game_context.IOProcessing;
import ru.nsu.zhdanov.lab_3.swing_facade.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Properties;

public class GameView extends JFrame {
  private MainController mainController;
  private Graphics2D graphicContext;
  private GameController gameController;
  private JPanel canvas;
  private JTextField weaponCondition;
  private JTextField livesQuantity;
  private JTextField curScore;
  private JTextField weaponName;

  GameView(Properties properties, MainController mainController) {
    this.gameController = new GameController(properties, this, mainController);
    super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    canvas = new JPanel(new FlowLayout(FlowLayout.CENTER));
    graphicContext = (Graphics2D) canvas.getGraphics();
    super.add(canvas);
  }

  private void initInput() {
    canvas.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        gameController.handleMousePressed(e);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        gameController.handleMouseTrack(e);
      }
    });
    canvas.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        gameController.handleMousePressed(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        gameController.handleMouseReleased(e);
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });
    super.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        gameController.handlePressedKey(e);
      }

      @Override
      public void keyReleased(KeyEvent e) {
        gameController.handleReleasedKey(e);
      }
    });
  }

  public Graphics2D getGraphicContext() {
    return graphicContext;
  }
}
