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
  private final int NUMBER_OF_PATTERN_LINES = 5;

  private final int SIZE_OF_FLOOR_LINE = 7;
  private final int[] FLOOR_LINE_PENALTIES = {-1,-1,-2,-2,-2,-3,-3};

  private WallBackgroundPattern wallBackgroundPattern;

  private List<Tile> floorLine;
  //contain the negative Tiles the player acquires during the drawing phase.

  public List<Tile> getFloorLine() {
    return floorLine;
  }

  public boolean[][] getWall() {
    return wall.clone();
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

    wallBackgroundPattern = new WallBackgroundPattern();

    initializePatternLines();


    //TODO: Test
    /*
    Offering offering = new FactoryDisplay();
    System.out.println("Offering content: " + offering.getContent());
    drawTiles(0, offering, 3);
    System.out.println("Content of first Line: " + patternLines[0][0]);
    System.out.println("Content of floor line: " + floorLine);

     */

    wall[1][1] = true;
    wall[1][2] = true;
    wall[1][4] = true;
    wall[0][3] = true;
    wall[2][3] = true;
    wall[4][3] = true;

    patternLines[1][0] = Tile.RED_TILE;
    patternLines[1][1] = Tile.RED_TILE;

    System.out.println(points);
    tileWallAndGetPoints();
    System.out.println(points);
  }

  public Tile[][] getPatternLines() {
    return patternLines.clone();
  }

  public WallBackgroundPattern getWallBackgroundPattern() {
    return wallBackgroundPattern;
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
   * @param pickedLineIndex the pattern line on which the tiles should be placed.
   * @param offering        the Offering from which the tiles should be drawn.
   * @param indexOfTile     the index of the tile in the Offering.
   * @return <true>true</true> if the tiles were successfully placed on the chosen line. <code>false</code> else.
   */
  boolean drawTiles(int pickedLineIndex, Offering offering, int indexOfTile) {
    if (!isValidPick(pickedLineIndex, offering, indexOfTile)) {
      return false;
    }

    //acquire the tiles from the chosen offering
    List<Tile> tiles = offering.takeTileWithIndex(indexOfTile);

    if (tiles.contains(Tile.STARTING_PLAYER_MARKER)) {
      if(floorLine.size() < SIZE_OF_FLOOR_LINE) {
        floorLine.add(Tile.STARTING_PLAYER_MARKER);
      }
      tiles.remove(Tile.STARTING_PLAYER_MARKER);
    }

    //if you picked too many tiles, so they don't all fit into the pattern line
    //put the tiles that you have too many of into the floor line.
    int remainingSizeOfLine = 0;
    for (int i = 0; i < patternLines[pickedLineIndex].length; i++) {
      if (patternLines[pickedLineIndex][i] == Tile.EMPTY_TILE) {
        remainingSizeOfLine++;
      } else {
        break;
      }
    }

    while (tiles.size() > remainingSizeOfLine) {
      if(floorLine.size() < SIZE_OF_FLOOR_LINE) {
        floorLine.add(tiles.get(0));
      }
      else{
        BagToStoreUsedTiles.getInstance().addTile(tiles.get(0));
      }
      tiles.remove(0);
    }

    //fill patternLine from right to left
    for (int i = pickedLineIndex; i > pickedLineIndex - tiles.size(); i--) {
      patternLines[pickedLineIndex][i] = tiles.get(0);
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

    //is on the wall in the same row already a tile with that color?
    if (wall[pickedLine][wallBackgroundPattern.indexOfTileInRow(pickedLine, tile)]) {
      return false;
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
    for (int rowNumber = 0; rowNumber < patternLines.length; rowNumber++) {
      Tile[] line = patternLines[rowNumber];
      //if the line is not completely full (leftmost field is empty), it gets ignored.
      if (line[0] == Tile.EMPTY_TILE) {
        continue;
      }

      //take the rightmost tile
      Tile tile = patternLines[rowNumber][rowNumber];

      //get the position of the tile on the wall pattern
      int indexOfTileOnWallPattern = wallBackgroundPattern.indexOfTileInRow(rowNumber, tile);

      //put it on the wall and delete the tile from the row
      assert (!wall[rowNumber][indexOfTileOnWallPattern]);
      wall[rowNumber][indexOfTileOnWallPattern] = true;
      line[rowNumber] = Tile.EMPTY_TILE;

      //get points
      points++;
      stepHorizontally(rowNumber, indexOfTileOnWallPattern, 1);
      stepHorizontally(rowNumber, indexOfTileOnWallPattern, -1);
      stepVertically(rowNumber, indexOfTileOnWallPattern, 1);
      stepVertically(rowNumber, indexOfTileOnWallPattern, -1);

      //put the rest of the tiles in the line into the BagToStoreUsedTiles
      //last position doesn't need to get checked since we already tiled that Tile to the wall.
      for (int j = 0; j < line.length - 1; j++) {
        if (line[j] != Tile.EMPTY_TILE) {
          BagToStoreUsedTiles.getInstance().addTile(line[j]);
          line[j] = Tile.EMPTY_TILE;
        }
      }
    }

    getMinusPointsForFloorLine();


    //TODO: Gib dem Spieler Punkte für die jeweiligen Fliesen ("sofort")
    //TODO: schreibe die negative Tiles um in Punkte. Also der erste negative Tile ist -1, der zweite ...
  }

  /**
   * Subtracts points from the player for every tile that is stored in the floor line.
   * The penalty points for this are defined in FLOOR_LINE_PENALTIES
   */
  private void getMinusPointsForFloorLine(){

  }

  /**
   * Recursive method that walks horizontally down the wall as long as the tile (specified by the
   * row and col values) has adjacent tiles on the wall. It then awards the player a point for that.
   * It is end-recursive, so it is just as performant as loops.
   *
   * @param row       the row position on the wall of the tile.
   * @param col       the col position on the wall of the tile.
   * @param direction the direction in which this method walks. <code>+1</code> if the method walks
   *                  to the right. <code>-1</code> if it walks to the left.
   */
  private void stepHorizontally(int row, int col, int direction) {
    if (((col + direction) >= 0)
        && ((col + direction) < wall[row].length)) {
      if (wall[row][col + direction]) {
        points++;
        stepHorizontally(row, col + direction, direction);
      }
    }
  }

  /**
   * Recursive method that walks vertically down the wall as long as the tile (specified by the row
   * and col values) has adjacent tiles on the wall. It then awards the player a point for that.
   * It is end-recursive, so it is just as performant as loops.
   *
   * @param row       the row position on the wall of the tile.
   * @param col       the col position on the wall of the tile.
   * @param direction the direction in which this method walks. <code>+1</code> if the method walks
   *                  down. <code>-1</code> if it walks up.
   */
  private void stepVertically(int row, int col, int direction) {
    if (((row + direction) >= 0)
        && ((row + direction) < wall.length)) {
      if (wall[row + direction][col]) {
        points++;
        stepHorizontally(row + direction, col, direction);
      }
    }
  }

  void addEndOfGameGetPoints() {

  }

  /**
   * clean up the PatternRows after each round
   */
  void dispose() {

  }
}
