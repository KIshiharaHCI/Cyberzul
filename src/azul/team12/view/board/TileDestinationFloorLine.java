package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;

public class TileDestinationFloorLine extends TileDestination {

  public TileDestinationFloorLine(int cell, int row, int cellSize,
      TileClickListener tileClickListener,
      ModelTile modelTile) {
    super(cell, row, cellSize, tileClickListener, modelTile);
  }
}
