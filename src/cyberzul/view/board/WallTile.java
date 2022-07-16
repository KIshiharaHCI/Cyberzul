package cyberzul.view.board;

import cyberzul.model.ModelTile;
import java.awt.Dimension;

/** WallTile without Tileclicklistener. Either set transparent or non-transparent. */
public class WallTile extends TileDecorator {

  private static final long serialVersionUID = 9L;

  /**
   * Constructor to be called from subclasses. Used for initializing Image URL path and cell XY
   * coordinates on the Wall.
   *
   * @param col X-Coordinate in given container
   * @param row Y-Coordinate in given container
   * @param modelTile contains the tile color information.
   */
  public WallTile(int col, int row, ModelTile modelTile, int tileSize, Float opacity) {
    super(col, row, modelTile, tileSize);
    setPreferredSize(new Dimension(tileSize, tileSize));
    setIcon(opacity);
  }
}
