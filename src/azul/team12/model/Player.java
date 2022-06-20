package azul.team12.model;

/**
 * This class contains the information that is depicted on the game board of the player.
 * I.e. his points, the tiles he already tiled and those who lie on his tiling fields.
 */
public class Player {
  private String name;
  private int points;

  private int negativeTiles;
      //contain the negative points the player acquires during the drawing phase.

  private boolean[][] wall;
      //the wall where the player tiles the tiles and gets his points ("Wand").

  private Tile[][] patternRows;
      //the left side on the board where the player places the tiles he draws ("Musterreihen").

  public Player(String name) {
    this.name = name;
    System.out.println("Creating player: "+name);
    this.points = 0;
    this.wall = new boolean[5][5];
    this.patternRows = initializePatternRows();
    patternRows = drawTiles();
  }

  /**
   * Initializes the patternRows with an empty two-dimensional array.
   * The first array has length 1. The second array has length 2. etc...
   *
   * @return the initialized empty patternRow
   */
  private Tile[][] initializePatternRows() {

    System.out.println("Initializing Pattern Rows");
    Tile[][] patternRows = new Tile[5][];
    patternRows[0] = new Tile[1];
    patternRows[1] = new Tile[2];
    patternRows[2] = new Tile[3];
    patternRows[3] = new Tile[4];
    patternRows[4] = new Tile[5];

    for (int column = 0; column < patternRows.length; column++) {
      for (int row = 0; row < patternRows[column].length; row++) {
        patternRows[column][row] = Tile.EMPTY_TILE;
        System.out.print(patternRows[column][row]+ ", ");
      }
      System.out.println("");
    }

    return patternRows.clone();
  }

  public String getName() {
    return name;
  }

  public int getPoints() {
    return points;
  }

  /**
   * represents drawing Tiles either from the TableCentre or from the Manufacturing Plates
   */
  public Tile[][] drawTiles(/*int pickedRow, int numberOfTilesOfGivenColor*/) {
    System.out.println("DrawTiles");
    //event listener: player chooses which row to place the tiles
    int pickedRow = 4; // is 5th row --- get the input from the view probably through some event listener
    int numberOfTilesOfGivenColor = 5; // get the input from the view

    // setting all
    for (int i = 0; i < numberOfTilesOfGivenColor; i++) {
      patternRows[pickedRow][i] = Tile.BLACK_TILE;
    }

    for (int column = 0; column < patternRows.length; column++) {
      for (int row = 0; row < patternRows[column].length; row++) {
        System.out.print(patternRows[column][row]+ ", ");
      }
      System.out.println("");
    }
    return patternRows;
  };

  /**
   * Represents the tiling phase, where all points are assigned.
   */
  public void tilingPhase(){
    //TODO: Gib dem Spieler Punkte für die jeweiligen Fliesen ("sofort")
    //TODO: schreibe die negative Tiles um in Punkte. Also der erste negative Tile ist -1, der zweite ...
    //TODO: leere die patternRows, aber nicht die wall
  }

  /**
   * clean up the PatternRows after each round
   */
  public void dispose() {/*todo*/}
}
