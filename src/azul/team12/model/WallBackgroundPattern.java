package azul.team12.model;

import static azul.team12.model.ModelTile.BLACK_TILE;
import static azul.team12.model.ModelTile.BLUE_TILE;
import static azul.team12.model.ModelTile.ORANGE_TILE;
import static azul.team12.model.ModelTile.RED_TILE;
import static azul.team12.model.ModelTile.WHITE_TILE;

/**
 * Contains a default wall background pattern aswell as methods to get information from it. Later it
 * will contain the possibility to create a personal background pattern.
 */
public class WallBackgroundPattern {

  /**
   * The pattern on the wall defining where which tiles may be placed.
   */
  private static final ModelTile[][] templateWall =
    {{BLUE_TILE, ORANGE_TILE, RED_TILE, BLACK_TILE, WHITE_TILE},
        {WHITE_TILE, BLUE_TILE, ORANGE_TILE, RED_TILE, BLACK_TILE},
        {BLACK_TILE, WHITE_TILE, BLUE_TILE, ORANGE_TILE, RED_TILE},
        {RED_TILE, BLACK_TILE, WHITE_TILE, BLUE_TILE, ORANGE_TILE},
        {ORANGE_TILE, RED_TILE, BLACK_TILE, WHITE_TILE, BLUE_TILE}};
  public final ModelTile[][] pattern;

  public WallBackgroundPattern() {
    this.pattern = getTemplateWall();
  }

  /**
   * Returns a copy of the template wall pattern.
   *
   * @return a copy of the template wall pattern.
   */
  public static ModelTile[][] getTemplateWall() {
    return templateWall.clone();
  }

  /**
   * Returns the index position of a tile in a specified row. e.g. 'On which position in row 3 is
   * the black tile?'
   *
   * @param rowNumber the number of the row of the wall.
   * @param tile      the tile that we are looking for.
   * @return the index number of that tile in the specific row.
   */
  public int indexOfTileInRow(int rowNumber, ModelTile tile) {
    int indexOfTilePosition = 0;
    for (int i = 0; i < pattern[rowNumber].length; i++) {
      if (pattern[rowNumber][i] == tile) {
        return i;
      }
    }
    return indexOfTilePosition;
  }
}
