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
  private final int[] FLOOR_LINE_PENALTIES = {-1, -1, -2, -2, -2, -3, -3};

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

    wall[1][1] = true;
    wall[1][2] = true;
    wall[1][4] = true;
    wall[0][3] = true;
    wall[2][3] = true;
    wall[4][3] = true;

    wall[1][0] = true;



    patternLines[1][0] = Tile.RED_TILE;
    patternLines[1][1] = Tile.RED_TILE;

    floorLine.add(Tile.STARTING_PLAYER_MARKER);
    floorLine.add(Tile.ORANGE_TILE);
    floorLine.add(Tile.BLUE_TILE);
    tileWallAndGetPoints();

    System.out.println(points);
    System.out.println(BagToStoreUsedTiles.getInstance().getContent());
    */
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
      if (floorLine.size() < SIZE_OF_FLOOR_LINE) {
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
      if (floorLine.size() < SIZE_OF_FLOOR_LINE) {
        floorLine.add(tiles.get(0));
      } else {
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
    //iterate through every line of patternLines
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
      int tilesInOneContiguousRow =
          getHorizontallyAdjacentTiles(rowNumber, indexOfTileOnWallPattern);
      int tilesInOneContiguousColumn =
          getVerticallyAdjacentTiles(rowNumber, indexOfTileOnWallPattern);

      if ((tilesInOneContiguousRow == 1) && (tilesInOneContiguousColumn == 1)) {
        //If the new tile has no neighbours, you only get one point.
        points++;
      } else {
        if (tilesInOneContiguousRow > 1) {
          points += tilesInOneContiguousRow;
        }
        if (tilesInOneContiguousColumn > 1) {
          points += tilesInOneContiguousColumn;
        }
      }

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
  }

  /**
   * Subtracts points from the player for every tile that is stored in the floor line.
   * The penalty points for this are defined in FLOOR_LINE_PENALTIES
   */
  private void getMinusPointsForFloorLine() {
    for (int i = 0; i < floorLine.size(); i++) {
      Tile tile = floorLine.get(i);
      if (tile == Tile.EMPTY_TILE) {
        break;
      }
      points += FLOOR_LINE_PENALTIES[i];
      if (tile != Tile.STARTING_PLAYER_MARKER) {
        BagToStoreUsedTiles.getInstance().addTile(tile);
      }
    }
    floorLine.clear();
  }

  /**
   * Get the number of horizontally adjacent Tiles of the tile that is specified by the row and
   * col values.
   *
   * @param row the row value of the tile that has just been tiled.
   * @param col the col value of the tile that has just been tiled.
   * @return the number of tiles that build a contiguous horizontal line with the new tile.
   */
  public int getHorizontallyAdjacentTiles(int row, int col) {
    int horizontallyContiguousTiles = 1;

    //move in the row to the right until you get to a tile that hasn't been tiled yet.
    while (((col + 1) < wall[row].length) && wall[row][col + 1]) {
      col++;
    }

    //count how far you can walk left in the row before you reach an entry that hasn't been tiled
    // yet
    while (((col - 1) >= 0) && wall[row][col - 1]) {
      col--;
      horizontallyContiguousTiles++;
    }

    return horizontallyContiguousTiles;
  }

  public int getVerticallyAdjacentTiles(int row, int col) {
    int verticallyContiguousTiles = 1;

    //move down in the column to the last contiguous tiled Tile in the column
    while (((row + 1) < wall.length) && wall[row + 1][col]) {
      row++;
    }

    //count how far you can walk up in the column before you reach an entry that hasn't been tiled
    // yet
    while (((row - 1) >= 0) && wall[row - 1][col]) {
      row--;
      verticallyContiguousTiles++;
    }

    return verticallyContiguousTiles;
  }

  void addEndOfGameGetPoints() {

  }
}
