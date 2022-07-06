package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The board that shows the pattern lines and the wall of each player. It also shows the name, the
 * points and the minus points of each player.
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
  private int minusPoints = 0;


  /**
   * The constructor to create the player board of the active player.
   *
   * @param tileClickListener
   */
  public PlayerBoard(Controller controller, TileClickListener tileClickListener,
      String playerName) {
    this.controller = controller;
    this.playerName = playerName;
    this.tileClickListener = tileClickListener;

    setLayout(new BorderLayout());
    center = new JPanel();
    center.setLayout(new GridLayout(1, 2));
    createCenterPanel();
    add(center, BorderLayout.CENTER);

    initializeClassVariables();
    addPointsAndPlayerNameElements();
    addMinusPointsFloorLineAndForfeitElements();
  }

  /**
   * Used by Constructor and disposeLabelsPatternLinesAndWall() Methods to create the current player
   * board. Refactored out of Constructor because playerBoard will be updated after every
   * NextPlayersTurnEvent.
   */
  private void createCenterPanel() {

    patternLines = new PatternLines(controller, Tile.TILE_SIZE, tileClickListener);
    center.add(patternLines);
    wall = new Wall(controller, tileClickListener);
    center.add(wall);
  }

  /**
   * Gets the information about the status of the current player such as name and score from
   * controller.
   */
  private void initializeClassVariables() {
    points = controller.getPoints(playerName);
    minusPoints = controller.getMinusPoints(playerName);
  }

  private void addPointsAndPlayerNameElements() {
    JPanel north = createNorthernPart("Points: ", points);
    north.add(new JLabel("Name: " + playerName));
    add(north, BorderLayout.NORTH);
  }

  private void addMinusPointsFloorLineAndForfeitElements() {
    JPanel south = new JPanel();
    south.setBackground(new Color(110, 150, 100));
    south.setLayout(new GridLayout(1, 4));
    south.add(new JLabel("   Minus Points: " + minusPoints));
    JButton floorLineButton = new JButton("Floor Line");
    floorLineButton.addActionListener(e -> {
      controller.placeTileAtFloorLine();
      controller.endTurn(controller.getNickOfActivePlayer());
    });
    south.add(floorLineButton);
    south.add(new JLabel(""));
    // TODO: Add "Do you really want to?"
    JButton forfeitGame = new JButton("Forfeit Game");
    forfeitGame.addActionListener(e -> {
      controller.forfeitGame();
    });
    south.add(forfeitGame, BorderLayout.SOUTH);
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
