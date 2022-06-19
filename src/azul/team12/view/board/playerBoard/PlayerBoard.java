package azul.team12.view.board.playerBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerBoard extends JPanel {
  private int points = 0;
  private int minusPoints = 0;
  private String playerName = "name";

  private PatternLines patternLines;

  private Wall wall;

  private int minBoardHeight = 180;
  private int minBoardWidth = 300;
  public PlayerBoard() {
    createBord();
    initialize();
  }
  public PlayerBoard(int minBoardHeight, int minBoardWidth) {
    this.minBoardHeight = minBoardHeight;
    this.minBoardWidth = minBoardWidth;
    createBordCurrent();
    initialize();
    this.patternLines.setButtonSize(50);
  }

  private void initialize() {

    setPreferredSize(new Dimension(minBoardWidth,minBoardHeight));
    setBackground(new Color(101,101,101));
  }
  private void createBordCurrent() {
    setLayout(new BorderLayout());
    createNorth();
    createSouth();
    createCenterCurrent();
  }



  private void createBord() {
    setLayout(new BorderLayout());
    createNorth();
    createSouth();
    createCenter();
  }

  private void createCenter() {
    JPanel center = new JPanel();
    center.setLayout(new GridLayout(1, 2));
    this.patternLines = new PatternLines();
    this.wall = new Wall();
    center.add(patternLines);
    center.add(wall);
    add(center, BorderLayout.CENTER);
  }
  private void createCenterCurrent() {
    JPanel center = new JPanel();
    center.setLayout(new GridLayout(1, 2));
    this.patternLines = new PatternLines(40);
    this.wall = new Wall();
    center.add(patternLines);
    center.add(wall);
    add(center, BorderLayout.CENTER);
  }

  private void createSouth() {
    JPanel south = new JPanel();
    south.setLayout(new GridLayout(1, 2));
    south.add(new JLabel("Minus Points: " + minusPoints));
    add(south, BorderLayout.SOUTH);
  }

  private void createNorth() {
    JPanel north = new JPanel();
    north.setLayout(new GridLayout(1,2));
    north.add(new JLabel("Points: " + points));
    north.add(new JLabel("Name: " + playerName));
    add(north, BorderLayout.NORTH);
  }
}
