package azul.team12.model;

/**
 * This class contains the information that is depicted on the game board of the player.
 * I.e. his points, the tiles he already tiled and those who lie on his tiling fields.
 */
public class Player {
  private String name;
  private int points;

  private boolean[][] wall; //the wall where the player tiles the tiles and gets his points ("Wand")

  private Tile[][] patternRows; //the left side on the board where the player places the tiles he draws ("Musterreihen")

  public Player(String name){
    this.name = name;
    this.points = 0;
    this.wall = new boolean[5][5];
  }

  /**
   * Initializes the patternRows with an empty two-dimensional array.
   * The first array has length 1. The second array has length 2. etc...
   * @return the initialized empty patternRow
   */
  private Tile[][] initializePatternRows(){
    return null;
  }

  public String getName() {
    return name;
  }

  public int getPoints() {
    return points;
  }
}
