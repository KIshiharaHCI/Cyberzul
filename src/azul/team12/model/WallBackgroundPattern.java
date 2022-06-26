package azul.team12.model;

import static azul.team12.model.Tile.BLACK_TILE;
import static azul.team12.model.Tile.BLUE_TILE;
import static azul.team12.model.Tile.ORANGE_TILE;
import static azul.team12.model.Tile.RED_TILE;
import static azul.team12.model.Tile.WHITE_TILE;

/**
 * Contains a default wall background pattern aswell as methods to get information from it.
 * Later it will contain the possibility to create a personal background pattern.
 */
public class WallBackgroundPattern {

  public final Tile[][] pattern;

  public WallBackgroundPattern(){
    this.pattern = getTemplateWall();
  }

  /**
   * The pattern on the wall defining where which tiles may be placed.
   */
  private static final Tile[][] templateWall =
      {{BLUE_TILE, ORANGE_TILE, RED_TILE, BLACK_TILE, WHITE_TILE},
          {WHITE_TILE, BLUE_TILE, ORANGE_TILE, RED_TILE, BLACK_TILE},
          {BLACK_TILE, WHITE_TILE, BLUE_TILE, ORANGE_TILE, RED_TILE},
          {RED_TILE, BLACK_TILE, WHITE_TILE, BLUE_TILE, ORANGE_TILE},
          {ORANGE_TILE, RED_TILE, BLACK_TILE, WHITE_TILE, BLUE_TILE}};

  /**
   * Returns a copy of the template wall pattern.
   *
   * @return a copy of the template wall pattern.
   */
  public static Tile[][] getTemplateWall() {
    return templateWall.clone();
  }

  /**
   * Returns the index position of a tile in a specified row. e.g. 'On which position in row 3 is
   * the black tile?'
   *
   * @param rowNumber the number of the row of the wall.
   * @param tile the tile that we are looking for.
   * @return the index number of that tile in the specific row.
   */
  public int indexOfTileInRow(int rowNumber, Tile tile){
    int indexOfTilePosition = 0;
    for(int i = 0; i < pattern[rowNumber].length; i++){
      if(pattern[rowNumber][i] == tile){
        return i;
      }
    }
    return indexOfTilePosition;
  }
}
