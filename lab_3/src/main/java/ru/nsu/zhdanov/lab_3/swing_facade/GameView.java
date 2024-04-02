package ru.nsu.zhdanov.lab_3.swing_facade;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

@Slf4j
public class GameView extends JFrame implements BaseViewInterface {
  private MainController mainController;
  private Graphics2D graphicContext;
  private GameController gameController;
  private Canvas canvas;
  private JTextField weaponCondition;
  private JTextField livesQuantity;
  private JTextField curScore;
  private JTextField weaponName;

  GameView(Properties properties, MainController mainController) {
    setLayout(new BorderLayout());
    this.canvas = new Canvas();
    this.curScore = new JTextField();
    this.weaponName = new JTextField();
    this.livesQuantity = new JTextField();
    this.weaponCondition = new JTextField();
    this.weaponCondition.setEditable(false);
    this.livesQuantity.setEditable(false);
    this.curScore.setEditable(false);
    this.weaponName.setEditable(false);
    this.canvas.setPreferredSize(new Dimension(1200, 675));
    add(canvas, BorderLayout.CENTER);

    JPanel weaponPanel = new JPanel(new GridLayout(2, 1));
    weaponPanel.add(weaponName);
    weaponPanel.add(weaponCondition);
    JPanel infoPanel = new JPanel(new GridLayout(2, 1));
    infoPanel.add(livesQuantity);
    infoPanel.add(curScore);
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    bottomPanel.add(weaponPanel);
    bottomPanel.add(infoPanel);
    this.add(bottomPanel, BorderLayout.SOUTH);

//    this.graphicContext = (Graphics2D) canvas.getGraphics();
    this.gameController = new GameController(properties, this, mainController);
    this.initInput();
  }

  private void initInput() {
    this.requestFocusInWindow(true);
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
        log.info("pressed key " + e.getButton());
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

    this.canvas.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
        log.info("keyPressed");
        gameController.handlePressedKey(e);
      }

      @Override
      public void keyReleased(KeyEvent e) {
        log.info("KeyReleased");
        gameController.handleReleasedKey(e);
      }
    });
  }

  public Graphics2D getGraphicContext() {
    return graphicContext;
  }

  @Override
  public void perform() {
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.requestFocusInWindow();
    this.canvas.requestFocus();
    this.setVisible(true);
    this.graphicContext = (Graphics2D) canvas.getGraphics();
    this.gameController.perform();
  }

  @Override
  public void shutdown() {
    this.gameController.shutdown();
    this.dispose();
  }
}
