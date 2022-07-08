package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.OnClickVisitor;
import azul.team12.view.listeners.TileClickListener;

/**
 * Subclass of TileDestination. Saves the information about each Tile such as the TileclickListener,
 * ImageIcons of Tiles, column and row.
 */
public class TileDestinationPatternLines extends TileDestination implements TileAcceptor {

  public TileDestinationPatternLines(int cell, int row, TileClickListener tileClickListener,
      ModelTile modelTile) {
    super(cell, row, tileClickListener, modelTile);
  }

  @Override
  public void acceptClick(OnClickVisitor visitor) {
    visitor.visitOnClick(this);

  }
}
