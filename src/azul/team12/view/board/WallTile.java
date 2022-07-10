package azul.team12.view.board;

import azul.team12.model.ModelTile;

/**
 * WallTile without Tileclicklistener. Either set transparent or non-transparent.
 */

public class WallTile extends TileDecorator {

  private static final long serialVersionUID = 9L;

  /**
   * Constructor to be called from subclasses. Used for initializing Image URL path and cell XY
   * coordinates on the Wall.
   *
   * @param col       X-Coordinate in given container
   * @param row       Y-Coordinate in given container
   * @param modelTile contains the tile color information.
   */
  public WallTile(int col, int row, ModelTile modelTile, Float opacity) {
    super(col, row, modelTile);
    setIcon(opacity);
  }
}
