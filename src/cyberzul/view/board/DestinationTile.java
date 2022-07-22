package cyberzul.view.board;

import cyberzul.model.ModelTile;
import cyberzul.view.listeners.OnClickVisitor;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Dimension;
import java.io.Serial;

/**
 * Tiles on the Pattern Line and the Floor Line.
 */
public class DestinationTile extends TileDecorator implements TileAcceptor {
  @Serial
  private static final long serialVersionUID = 3L;

  /**
   * Constructor for the Pattern line and the floor line.
   *
   * @param col               The col of the {@link DestinationTile}.
   * @param row               The row of the {@link DestinationTile}.
   * @param modelTile         The model {@link DestinationTile}.
   * @param listener          The {@link TileClickListener} to move the tiles.
   * @param preferredTileSize the size to set the tile to.
   */
  public DestinationTile(
      int col, int row, ModelTile modelTile, TileClickListener listener, int preferredTileSize) {
    super(col, row, modelTile, preferredTileSize);
    setPreferredSize(new Dimension(preferredTileSize, preferredTileSize));

    setIcon(1f);
    addMouseListener(listener);
  }

  @Override
  public void acceptClick(OnClickVisitor visitor) {
    visitor.visitOnClick(this);
  }
}
