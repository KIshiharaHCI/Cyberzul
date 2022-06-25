package azul.team12.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the information that is depicted on the game board of the player.
 * I.e. his points, the tiles he already tiled and those who lie on his tiling fields.
 */
public class Player {
  private String name;
  private int points;
  private int NUMBER_OF_PATTERN_LINES = 5;

  private List<Tile> floorLine;
  //contain the negative Tiles the player acquires during the drawing phase.

  public List<Tile> getFloorLine() {
    return floorLine;
  }

  public boolean[][] getWall() {
    return wall.clone();
  }

  public Tile[][] getPatternLines() {
    return patternLines.clone();
  }

  private boolean[][] wall;
  //the wall where the player tiles the tiles and gets his points ("Wand").

  private Tile[][] patternLines;
  //the left side on the board where the player places the tiles he draws ("Musterreihen").

  Player(String name) {
    this.name = name;

    this.points = 0;
    this.floorLine = new ArrayList<>();
    this.wall = new boolean[5][5];
    initializePatternLines();

    /*
    //TODO: Test
    Offering offering = new FactoryDisplay();
    System.out.println(drawTiles(4, offering, 3));
    drawTiles(3, TableCenter.getInstance(), 0);

    //should yield false because the offering is already empty
    System.out.println(drawTiles(0, offering, 1));

    System.out.println(drawTiles(0,new FactoryDisplay(),2));
    //should yield false because there is already a tile on this field
    System.out.println(drawTiles(0,new FactoryDisplay(), 1));

     */
  }

  /**
   * Initializes the patternRows with an empty two-dimensional array.
   * The first array has length 1. The second array has length 2. etc...
   *
   * @return the initialized empty patternRow
   */
  private void initializePatternLines() {

    this.patternLines = new Tile[NUMBER_OF_PATTERN_LINES][];
    for (int i = 0; i < 5; i++) {
      patternLines[i] = new Tile[i + 1];
    }

    for (int column = 0; column < patternLines.length; column++) {
      for (int row = 0; row < patternLines[column].length; row++) {
        patternLines[column][row] = Tile.EMPTY_TILE;
        //TODO: Test output
        //System.out.print(patternLines[column][row] + ", ");
      }
      //System.out.println("");
    }

  }

  public String getName() {
    return name;
  }

  public int getPoints() {
    return points;
  }

  /**
   * Draw Tiles from an Offering and place them on the chosen pattern line.
   *
   * @param pickedLine  the pattern line on which the tiles should be placed.
   * @param offering    the Offering from which the tiles should be drawn.
   * @param indexOfTile the index of the tile in the Offering.
   * @return <true>true</true> if the tiles were successfully placed on the chosen line. <code>false</code> else.
   */
  boolean drawTiles(int pickedLine, Offering offering, int indexOfTile) {
    if (!isValidPick(pickedLine, offering, indexOfTile)) {
      return false;
    }

    //acquire the tiles from the chosen offering
    List<Tile> tiles = offering.takeTileWithIndex(indexOfTile);

    if (tiles.contains(Tile.STARTING_PLAYER_MARKER)) {
      floorLine.add(Tile.STARTING_PLAYER_MARKER);
      tiles.remove(Tile.STARTING_PLAYER_MARKER);
    }

    for (int i = pickedLine; i > pickedLine - tiles.size(); i--) {
      patternLines[pickedLine][i] = tiles.get(0);
    }

    /*
    //TODO: Test output
    for (int column = 0; column < patternLines.length; column++) {
      for (int row = 0; row < patternLines[column].length; row++) {
        System.out.print(patternLines[column][row] + ", ");
      }
      System.out.println("");
    }

     */
    return true;
  }

  /**
   * Check if it's possible to draw tiles from the offering and to place them on the desired row.
   *
   * @param pickedLine  the pattern line on which the tiles should be placed.
   * @param offering    the Offering from which the tiles should be drawn.
   * @param indexOfTile the index of the tile in the Offering.
   * @return <code>true</code> if the chosen tile can be placed on the chosen line. <code>false</code> else.
   */
  private boolean isValidPick(int pickedLine, Offering offering, int indexOfTile) {
    List<Tile> tiles = offering.getContent();
    Tile tile;

    //does this Offering contain any tileable tiles?
    if (tiles.contains(Tile.STARTING_PLAYER_MARKER)) {
      if (tiles.size() == 1) {
        return false;
      } else {
        tile = tiles.get(indexOfTile + 1);
      }
    } else {
      if (tiles.size() == 0) {
        return false;
      } else {
        tile = tiles.get(indexOfTile);
      }
    }

    //are there free places on the selected row? Only first position of the line has to be checked.
    if (patternLines[pickedLine][0] != Tile.EMPTY_TILE) {
      return false;
    }

    //is the color of the selected tile compatible with the tiles that already lay on the
    //only last position has to be checked.
    if ((patternLines[pickedLine][pickedLine] != tile)
        && patternLines[pickedLine][pickedLine] != Tile.EMPTY_TILE) {
      return false;
    }

    return true;
  }

  /**
   * Represents the tiling phase, where all points are assigned.
   */
  void tileWallAndGetPoints() {
    //TODO: Gib dem Spieler Punkte für die jeweiligen Fliesen ("sofort")
    //TODO: schreibe die negative Tiles um in Punkte. Also der erste negative Tile ist -1, der zweite ...
    //TODO: leere die patternRows, aber nicht die wall

    initializePatternLines();
  }

  void addEndOfGameGetPoints() {

  }

  /**
   * clean up the PatternRows after each round
   */
  void dispose() {

  }
}
