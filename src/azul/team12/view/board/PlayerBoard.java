package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;

/**
 * The board that shows the pattern lines and the wall of each player. It also shows the name, the
 * points and the minus points of each player.
 */
public class PlayerBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private transient final Controller controller;
  private PatternLines patternLines;
  private Wall wall;
  private transient TileClickListener tileClickListener;
  private String playerName;
  private JPanel center;
  private int points;
  private int minusPoints = 0;
  private JButton forfeitButton, cancelGameButton, restartGameButton;


  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param tileClickListener
   */
  public PlayerBoard(Controller controller, TileClickListener tileClickListener,
      String playerName) {
    this.controller = controller;
    this.playerName = playerName;
    this.tileClickListener = tileClickListener;

    setLayout(new BorderLayout());
    JPanel coverOverCenter = new JPanel();
    coverOverCenter.setBackground(new Color(110, 150, 100));
    center = new JPanel();
    //center.setLayout(new GridLayout(1, 2));
    createCenterPanel();
    coverOverCenter.add(center);
    add(coverOverCenter, BorderLayout.CENTER);

    initializeClassVariables();
    initializeButtons();
    addPointsAndPlayerNameElements();
    addMinusPointsElements();
  }

  /**
   * Used by Constructor and disposeLabelsPatternLinesAndWall() Methods to create the current player
   * board. Refactored out of Constructor because playerBoard will be updated after every
   * NextPlayersTurnEvent.
   */
  private void createCenterPanel() {
    setProperties(Tile.TILE_SIZE, 5, 10, center);
    patternLines = new PatternLines(controller, Tile.TILE_SIZE, tileClickListener);
    center.add(patternLines);
    wall = new Wall(controller,tileClickListener);
    center.add(wall);
  }

  void setProperties(int tileSize, int rows, int cols, JPanel panel) {
    panel.setBackground(new Color(110, 150, 100));

    panel.setPreferredSize(new Dimension((tileSize + 2) * cols, (tileSize + 2) * rows));
    panel.setMaximumSize(new Dimension((tileSize + 2) * cols, (tileSize + 2) * rows));
    panel.setMinimumSize(new Dimension((tileSize + 2) * cols, (tileSize + 2) * rows));
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.setAlignmentX(0.5f);
    panel.setAlignmentY(1.0f);
  }

  /**
   * Gets the information about the status of the current player such as name and score from
   * controller.
   */
  private void initializeClassVariables() {
    points = controller.getPoints(playerName);
    minusPoints = controller.getMinusPoints(playerName);
  }

  /**
   * initializes Forfeit Button
   */
  private void initializeButtons() {
    forfeitButton = new JButton("FORFEIT");
    forfeitButton.setPreferredSize(new Dimension(25,10));
    forfeitButton.addActionListener(event -> {
      controller.replaceActivePlayerByAI();
      System.out.println("Forfeit Button has been clicked");
    });

    cancelGameButton = new JButton("CANCEL");
    cancelGameButton.addActionListener(event -> {
      controller.cancelGameForAllPlayers();
      System.out.println("Cancel Button has been pressed.");
    });

    restartGameButton = new JButton("RESTART");
    restartGameButton.addActionListener(event -> {
      controller.restartGame();
      System.out.println("Game has been restarted.");
    });
  }

  private void addPointsAndPlayerNameElements() {
    JPanel north = createNorthernPart("Points: ", points);
    north.add(new JLabel("Name: " + playerName));
    north.add(forfeitButton);
    north.add(cancelGameButton);
    north.add(restartGameButton);
    add(north, BorderLayout.NORTH);
  }

  private void addMinusPointsElements() {
    FloorLinePanel south = new FloorLinePanel(controller, tileClickListener, minusPoints);
    add(south, BorderLayout.SOUTH);


  }

  private JPanel createNorthernPart(String x, int minusPoints) {
    JPanel north = new JPanel();
    north.setBackground(new Color(110, 150, 100));
    north.setLayout(new GridLayout(1, 2));
    north.add(new JLabel("   " + x + minusPoints));
    return north;
  }
}
