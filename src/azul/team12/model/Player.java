package azul.team12.model;
//TODO: Marco - game finished checker
import static azul.team12.model.ModelTile.EMPTY_TILE;
import static azul.team12.model.ModelTile.STARTING_PLAYER_MARKER;

import java.util.ArrayList;
import java.util.List;
/**
 * This class contains the information that is depicted on the game board of the player.
 * I.e. his points, the tiles he already tiled and those who lie on his tiling fields.
 */
public class Player {
  private String name;
  private int points;
  public static final int NUMBER_OF_PATTERN_LINES = 5;

  public static final int SIZE_OF_FLOOR_LINE = 7;
  private static final int[] FLOOR_LINE_PENALTIES = {-1, -1, -2, -2, -2, -3, -3};
  public static final int POINTS_FOR_COMPLETE_HORIZONTAL_LINE = 2;
  public static final int POINTS_FOR_COMPLETE_VERTICAL_LINE = 7;
  public static final int POINTS_FOR_PLACING_ALL_STONES_OF_ONE_COLOR = 10;

  private boolean hasStartingPlayerMarker = false;
  private boolean isAIPlayer = false;
  private boolean hasEndedTheGame = false;

  private WallBackgroundPattern wallPattern;

  private ArrayList<ModelTile> floorLine;
  //contain the negative Tiles the player acquires during the drawing phase.

  public List<ModelTile> getFloorLine() {
    return (List<ModelTile>) floorLine.clone();
  }

  public boolean[][] getWall() {
    return wall.clone();
  }

  private boolean[][] wall;
  //the wall where the player tiles the tiles and gets his points ("Wand").

  private ModelTile[][] patternLines;
  //the left side on the board where the player places the tiles he draws ("Musterreihen").

  Player(String name) {
    this.name = name;

    this.points = 0;
    this.floorLine = new ArrayList<>();
    this.wall = new boolean[5][5];

    wallPattern = new WallBackgroundPattern();

    initializePatternLines();

  }

  public ModelTile[][] getPatternLines() {
    return patternLines.clone();
  }

  //TODO: Create PatternLinesModel class and make this method to a @Override toString() in it;
  /**
   * get the Pattern Lines as a string with a column width of 15 characters (for testing purposes)
   *
   * @return the pattern lines as string
   */
  public String getPatterLinesAsString() {
    ModelTile[][] patternLines = getPatternLines();
    String patternLinesAsString = "\n";
    ModelTile currentModelTile;
    int columWidthInCharacters = 15;
    for (int row = 0; row < patternLines.length; row++) {
      for (int column = 0; column < patternLines[row].length; column++) {
        patternLinesAsString += patternLines[row][column].toString();
        currentModelTile = patternLines[row][column];
        for (int i = currentModelTile.toString().length(); i < columWidthInCharacters; i++) {
          patternLinesAsString += " ";
        }
        patternLinesAsString += "| ";
      }
      patternLinesAsString += "\n";
    }
    return patternLinesAsString;
  }

  public WallBackgroundPattern getWallPattern() {
    return wallPattern;
  }

  public boolean hasStartingPlayerMarker() {
    return hasStartingPlayerMarker;
  }

  public boolean isAIPlayer() {
    return isAIPlayer;
  }

  public void setAIPlayer(boolean aIPlayer) {
    isAIPlayer = aIPlayer;
  }

  public boolean hasEndedTheGame() {
    return hasEndedTheGame;
  }

  /**
   * Initializes the patternRows with an empty two-dimensional array.
   * The first array has length 1. The second array has length 2. etc...
   *
   * @return the initialized empty patternRow
   */
  private void initializePatternLines() {

    this.patternLines = new ModelTile[NUMBER_OF_PATTERN_LINES][];
    for (int i = 0; i < 5; i++) {
      patternLines[i] = new ModelTile[i + 1];
    }

    for (int column = 0; column < patternLines.length; column++) {
      for (int row = 0; row < patternLines[column].length; row++) {
        patternLines[column][row] = EMPTY_TILE;
      }
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
   * @param row         the pattern line on which the tiles should be placed.
   * @param offering    the Offering from which the tiles should be drawn.
   * @param indexOfTile the index of the tile in the Offering.
   * @return <true>true</true> if the tiles were successfully placed on the chosen line.
   * <code>false</code> else.
   */
  boolean drawTiles(int row, Offering offering, int indexOfTile) {
    //check if it's possible to place the chosen tile on the chosen line
    //this doesn't yet change the state of the data model!
    if (!isValidPick(row, offering, indexOfTile)) {
      return false;
    }

    //TODO: make this methods more readable @Nils
    //Hotfix
    //mid-development view design changes made the StartingPlayerMarker clickable
    // -> it has its own index
    //since the methods were designs so that it is not clickable these 3 lines fix the bug where
    //the StartPlayerMarker gets removed -> the content of the TableCenter gets smaller and picking
    //the last tile of the table center causes an OutOfBoundsExceptions
    if(offering.getContent().contains(STARTING_PLAYER_MARKER)){
      indexOfTile--;
    }

    //acquire the tiles from the chosen offering
    List<ModelTile> pickedTiles = offering.takeTileWithIndex(indexOfTile);

    //the cursor says on which position we currently are in the patternLine.
    int cursor = row;
    while (pickedTiles.size() > 0) {
      //store the SPM somewhere
      if (pickedTiles.get(0) == STARTING_PLAYER_MARKER) {
        hasStartingPlayerMarker = true;
        fillFloorLine(pickedTiles.remove(0));
      } else {
        //the
        if (patternLines[row][cursor] == EMPTY_TILE) {
          patternLines[row][cursor] = pickedTiles.remove(0);
        } else {
          if ((cursor - 1) >= 0) {
            cursor--;
          } else {
            fillFloorLine(pickedTiles.remove(0));
          }
        }
      }
    }
    return true;
  }

  /**
   * Draw Tiles from an Offering and place them directly to the floor Line.
   *
   * @param offering    the Offering from which the tiles should be drawn.
   * @param indexOfTile the index of the tile in the Offering.
   */
  void placeTileInFloorLine(Offering offering, int indexOfTile) {
    //TODO: make this methods more readable @Nils
    //Hotfix
    //mid-development view design changes made the StartingPlayerMarker clickable
    // -> it has its own index
    //since the methods were designs so that it is not clickable these 3 lines fix the bug where
    //the StartPlayerMarker gets removed -> the content of the TableCenter gets smaller and picking
    //the last tile of the table center causes an OutOfBoundsExceptions
    if(offering.getContent().contains(STARTING_PLAYER_MARKER)){
      indexOfTile--;
    }

    //acquire the tiles from the chosen offering
    List<ModelTile> pickedTiles = offering.takeTileWithIndex(indexOfTile);



    for(ModelTile modelTile : pickedTiles) {
      System.out.println("Model Tile in offering: " + modelTile);
    }
    while (pickedTiles.size() > 0) {
      fillFloorLine(pickedTiles.remove(0));
    }

  }

  /**
   * This method represents the tiles that fall on the ground. If the floorline has still enough
   * places, the fallen tile gets added on the floor line. Else it gets transfered into the
   * BagToStoreUsedModelTiles.
   *
   * @param tile the tile that should be stored in the floorline.
   */
  private void fillFloorLine(ModelTile tile) {
    if (floorLine.size() < SIZE_OF_FLOOR_LINE) {
      floorLine.add(tile);
    } else {
      BagToStoreUsedTiles.getInstance().addTile(tile);
    }
  }

  /**
   * Check if it's possible to draw tiles from the offering and to place them on the desired row.
   *
   * @param pickedLine  the pattern line on which the tiles should be placed.
   * @param offering    the Offering from which the tiles should be drawn.
   * @param indexOfTile the index of the tile in the Offering.
   * @return <code>true</code> if the chosen tile can be placed on the chosen line.
   * <code>false</code> else.
   */
  boolean isValidPick(int pickedLine, Offering offering, int indexOfTile) {
    List<ModelTile> tiles = offering.getContent();
    ModelTile tile;

    //does this Offering contain any tileable tiles?
    if (tiles.contains(STARTING_PLAYER_MARKER)) {
      if (tiles.size() == 1) {
        return false;
      } else {
        tile = tiles.get(indexOfTile);
      }
    } else {
      if (tiles.size() == 0) {
        return false;
      } else {
        tile = tiles.get(indexOfTile);
      }
    }

    //is on the wall in the same row already a tile with that color?
    if (wall[pickedLine][wallPattern.indexOfTileInRow(pickedLine, tile)]) {
      System.out.println("Reason for FALSE is that on the same row already exists "
          + "a tile with that color.");
      return false;
    }

    //are there free places on the selected row? Only first position of the line has to be checked.
    if (patternLines[pickedLine][0] != EMPTY_TILE) {
      System.out.println("Reason for FALSE is that there are now free places on that row.");
      return false;
    }

    //is the color of the selected tile compatible with the tiles that already lay on the
    //only last position has to be checked.
    if ((patternLines[pickedLine][pickedLine] != tile)
        && patternLines[pickedLine][pickedLine] != EMPTY_TILE) {
      System.out.println("Reason for FALSE is that the tile color is not compatible with "
          + "other tiles on that line. ");
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
      ModelTile[] line = patternLines[rowNumber];
      //if the line is not completely full (leftmost field is empty), it gets ignored.
      if (line[0] == EMPTY_TILE) {
        continue;
      }

      //take the rightmost tile
      ModelTile tile = patternLines[rowNumber][rowNumber];

      //get the position of the tile on the wall pattern
      int indexOfTileOnWallPattern = wallPattern.indexOfTileInRow(rowNumber, tile);

      //put it on the wall and delete the tile from the row
      assert (!wall[rowNumber][indexOfTileOnWallPattern]);
      wall[rowNumber][indexOfTileOnWallPattern] = true;
      line[rowNumber] = EMPTY_TILE;

      //get points
      int tilesInOneContiguousRow =
          getHorizontallyAdjacentTiles(rowNumber, indexOfTileOnWallPattern);
      int tilesInOneContiguousColumn =
          getVerticallyAdjacentTiles(rowNumber, indexOfTileOnWallPattern);

      if (tilesInOneContiguousRow == 5) {
        hasEndedTheGame = true;
      }

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
        if (line[j] != EMPTY_TILE) {
          BagToStoreUsedTiles.getInstance().addTile(line[j]);
          line[j] = EMPTY_TILE;
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
      ModelTile tile = floorLine.get(i);
      if (tile == EMPTY_TILE) {
        break;
      }
      points += FLOOR_LINE_PENALTIES[i];
      if (tile != STARTING_PLAYER_MARKER) {
        BagToStoreUsedTiles.getInstance().addTile(tile);
      }
    }
    floorLine.clear();
  }

  //TODO: This method gets obsolete if we implemented the visible floor line.
  /**
   * Returns the number of MinusPoints that the player acquired over te
   *
   * @return
   */
  public int getMinusPoints(){
    int minusPoints = 0;
    for (int i = 0; i < floorLine.size(); i++) {
      ModelTile tile = floorLine.get(i);
      if (tile == EMPTY_TILE) {
        break;
      }
      minusPoints += FLOOR_LINE_PENALTIES[i];
    }
    return minusPoints;
  }

  /**
   * Get the number of horizontally adjacent Tiles of the tile that is specified by the row and
   * col values.
   *
   * @param row the row value of the tile that has just been tiled.
   * @param col the col value of the tile that has just been tiled.
   * @return the number of tiles that build a contiguous horizontal line with the new tile.
   */
  private int getHorizontallyAdjacentTiles(int row, int col) {
    int horizontallyConsecutiveTiles = 1;

    //move in the row to the right until you get to a tile that hasn't been tiled yet.
    while (((col + 1) < wall[row].length) && wall[row][col + 1]) {
      col++;
    }

    //count how far you can walk left in the row before you reach an entry that hasn't been tiled
    // yet
    while (((col - 1) >= 0) && wall[row][col - 1]) {
      col--;
      horizontallyConsecutiveTiles++;
    }

    return horizontallyConsecutiveTiles;
  }

  /**
   * Get the number of vertically adjacent Tiles of the tile that is specified by the row and
   * col values.
   *
   * @param row the row value of the tile that has just been tiled.
   * @param col the col value of the tile that has just been tiled.
   * @return the number of tiles that build a contiguous horizontal line with the new tile.
   */
  private int getVerticallyAdjacentTiles(int row, int col) {
    int verticallyConsecutiveTiles = 1;

    //move down in the column to the last contiguous tiled Tile in the column
    while (((row + 1) < wall.length) && wall[row + 1][col]) {
      row++;
    }

    //count how far you can walk up in the column before you reach an entry that hasn't been tiled
    // yet
    while (((row - 1) >= 0) && wall[row - 1][col]) {
      row--;
      verticallyConsecutiveTiles++;
    }

    return verticallyConsecutiveTiles;
  }

  /**
   * Award additional points to the player because the game ended.
   */
  void addEndOfGamePoints() {
    //gain points for each complete horizontal line of 5 consecutive tiles on the wall
    int amountOfRows = wall.length;
    int amountOfCols = wall[0].length;
    for (int row = 0; row < amountOfRows; row++) {
      for (int col = 0; col < amountOfCols; col++) {
        if (!wall[row][col]) {
          break;
        }
        if (col == amountOfCols - 1) {
          points += POINTS_FOR_COMPLETE_HORIZONTAL_LINE;
        }
      }
    }

    //gain points for each complete vertical line of 5 consecutive tiles on the wall
    for (int col = 0; col < amountOfCols; col++) {
      for (int row = 0; row < amountOfRows; row++) {
        if (!wall[row][col]) {
          break;
        }
        if (row == amountOfRows - 1) {
          points += POINTS_FOR_COMPLETE_VERTICAL_LINE;
        }
      }
    }

    //gain 10 points for each color of which you have placed all 5 tiles on your wall
    List<ModelTile> tilableTiles = ModelTile.valuesOfTilableTiles();
    for (ModelTile tile : tilableTiles) {
      for (int row = 0; row < wall.length; row++) {
        if (!wall[row][wallPattern.indexOfTileInRow(row, tile)]) {
          break;
        }
        if (row == (wall.length - 1)) {
          points += POINTS_FOR_PLACING_ALL_STONES_OF_ONE_COLOR;
        }
      }
    }
  }
}
