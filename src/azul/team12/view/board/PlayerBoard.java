package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The board that shows the pattern lines and the wall of each player. It also shows the name,
 * the points and the minus points of each player.
 */
public class PlayerBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final Controller controller;
  private PatternLines patternLines;
  private Wall wall;
  private TileClickListener tileClickListener;
  private String playerName;
  private JPanel center;
  private int points;
  //Hardcoded Variable only used by opponent boards constructor
  private final int minusPoints = 0;
  /**
   * The constructor to create one og the player boards of the opponents.
   */
  public PlayerBoard(Controller controller) {
    this.controller = controller;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(0.5f);
    setAlignmentY(1.0f);

    initializeClassVariables();
    createOthersFirst();

    final JPanel center = new JPanel();
    center.setMaximumSize(new Dimension(300, 260));
    center.setLayout(new GridLayout(1, 2));
    this.patternLines = new PatternLines(controller);
    center.add(patternLines);
    this.wall = new Wall(controller);
    center.add(wall);
    add(center);
    createOthersLast();
  }

  /**
   * The constructor to create the player board of the active player.
   *
   * @param tileSize
   * @param tileClickListener
   */
  public PlayerBoard(Controller controller, int tileSize, TileClickListener tileClickListener) {
    this.controller = controller;
    this.tileClickListener = tileClickListener;

    setLayout(new BorderLayout());
    center = new JPanel();
    center.setLayout(new GridLayout(1, 2));
    createCenterPanel();
    add(center, BorderLayout.CENTER);

    initializeClassVariables();
    createNorth();
    addMinusPointsElements();
  }

  /**
   * Used by Constructor and disposeLabelsPatternLinesAndWall() Methods to create the current player board.
   * Refactored out of Constructor because playerBoard will be updated after every NextPlayersTurnEvent.
   */
  private void createCenterPanel() {

    patternLines = new PatternLines(controller,Tile.TILE_SIZE,tileClickListener);
    center.add(patternLines);
    wall = new Wall(controller,tileClickListener);
    center.add(wall);
  }

  /**
   * Gets the information about the status of the current player such as name and score from controller.
   */
  private void initializeClassVariables() {
    playerName = controller.getNickOfActivePlayer();
    points = controller.getPoints(playerName);
  }

  /**
   * Opponents Constructor invokes this method in order to add the graphical elements for the
   * points and the
   *
   */
  private void createOthersFirst() {
    JPanel north = addPointsAndPlayerNameElements();
    add(north);
  }

  /**
   * Opponents Constructor invokes this method in order to add Minus Points to the game board.
   */
  private void createOthersLast() {
    JPanel south = new JPanel();
    south.setBackground(new Color(110, 150, 100));
    south.setLayout(new GridLayout(1, 2));
    south.add(new JLabel("Minus Points: " + minusPoints));
    add(south);
  }

  /**
   * Active player constructor invokes this method
   */
  private void addMinusPointsElements() {
    JPanel south = createSouthernPart("Minus Points: ", minusPoints);
    add(south, BorderLayout.SOUTH);
  }

  private JPanel addPointsAndPlayerNameElements() {
    JPanel north = createSouthernPart("Points: ", points);
    north.add(new JLabel("Name: " + playerName));
    return north;
  }

  private JPanel createSouthernPart(String x, int minusPoints) {
    JPanel south = new JPanel();
    south.setBackground(new Color(110, 150, 100));
    south.setLayout(new GridLayout(1, 2));
    south.add(new JLabel(x + minusPoints));
    return south;
  }

  /**
   * Active player constructor invokes this method.
   */
  private void createNorth() {
    JPanel north = addPointsAndPlayerNameElements();
    add(north, BorderLayout.NORTH);
  }
}
