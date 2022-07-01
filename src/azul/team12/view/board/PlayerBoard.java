package azul.team12.view.board;

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
  private final PatternLines patternLines;
  private final Wall wall;
  private int points = 0;
  private int minusPoints = 0;
  private String playerName = "name";

  /**
   * The constructor to create one og the player boards of the opponents.
   */
  public PlayerBoard() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(0.5f);
    setAlignmentY(1.0f);
    createOthersFirst();
    final JPanel center = new JPanel();
    center.setMaximumSize(new Dimension(300, 260));
    center.setLayout(new GridLayout(1, 2));
    this.patternLines = new PatternLines();
    center.add(patternLines);
    this.wall = new Wall();
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
  public PlayerBoard(int tileSize, TileClickListener tileClickListener) {

    setLayout(new BorderLayout());
    final JPanel center = new JPanel();
    center.setLayout(new GridLayout(1, 2));
    this.patternLines = new PatternLines(tileSize, tileClickListener);
    center.add(patternLines);
    this.wall = new Wall(tileSize, tileClickListener);
    center.add(wall);
    add(center, BorderLayout.CENTER);

    createNorth();
    addMinusPointsElements();
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
