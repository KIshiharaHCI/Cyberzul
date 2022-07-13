package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The board that shows the pattern lines and the wall of each player. It also shows the name, the
 * points and the minus points of each player.
 */
public abstract class PlayerBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  protected transient final Controller controller;
  private PatternLines patternLines;
  private Wall wall;
  private transient TileClickListener tileClickListener;
  private String playerName;
  private JPanel patternLinesAndWallPanel;
  protected JPanel north;
  private int points;
  private int minusPoints = 0;
  protected int tileSize;
  Dimension panelDimension;
  //Wraps PatternLines, Wall, Points Labels, Playername, Floorline
  private JPanel playerBoardWrapper;

  private FloorLinePanel floorLinePanel;

  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param tileClickListener
   */
  public PlayerBoard(Controller controller, TileClickListener tileClickListener,
      String playerName, int tileSize, Dimension panelDimension) {
    this.controller = controller;
    this.playerName = playerName;
    this.tileClickListener = tileClickListener;
    this.tileSize = tileSize;
    this.panelDimension = panelDimension;

    setPlayerBoardWrapperSize();
    createPatternLinesAndWallPanel();
    playerBoardWrapper.add(patternLinesAndWallPanel, BorderLayout.CENTER);
    add(playerBoardWrapper, BorderLayout.CENTER);

    initializeClassVariables();

    addPointsAndPlayerNameElements();
    addMinusPointsElements();
  }

  void setPlayerBoardWrapperSize (){
    playerBoardWrapper = new JPanel(new BorderLayout());
    Dimension wrapperDimension = new Dimension(
            (int) (panelDimension.width * 0.87),
            (int) (panelDimension.height * 0.55)
    );

    playerBoardWrapper.setMaximumSize(wrapperDimension);
    playerBoardWrapper.setMinimumSize(wrapperDimension);
    playerBoardWrapper.setOpaque(false);
  }

  /**
   * Used by Constructor and disposeLabelsPatternLinesAndWall() Methods to create the current player
   * board. Refactored out of Constructor because playerBoard will be updated after every
   * NextPlayersTurnEvent.
   */
  private void createPatternLinesAndWallPanel() {
    patternLinesAndWallPanel = new JPanel();
    setProperties(tileSize, 5, 10, patternLinesAndWallPanel);
    patternLines = new PatternLines(playerName, controller, tileSize, tileClickListener);
    patternLinesAndWallPanel.add(patternLines);
    wall = new Wall(playerName, controller, tileSize, tileClickListener);
    patternLinesAndWallPanel.add(wall);
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

  private void addPointsAndPlayerNameElements() {
    north = createNorthernPart("Points: ", points);
    north.add(new JLabel("Name: " + playerName));
    playerBoardWrapper.add(north, BorderLayout.NORTH);
  }

  private void addMinusPointsElements() {
    floorLinePanel = new FloorLinePanel(playerName, controller, tileClickListener, minusPoints);
    playerBoardWrapper.add(floorLinePanel, BorderLayout.SOUTH);


  }

  private JPanel createNorthernPart(String x, int minusPoints) {
    JPanel north = new JPanel();
    north.setBackground(new Color(110, 150, 100));
    north.setLayout(new GridLayout(1, 1));
    north.add(new JLabel("   " + x + minusPoints));
    return north;
  }

  public FloorLinePanel getFloorLinePanel() {
    return floorLinePanel;
  }
}
