package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.OnClickVisitor;
import azul.team12.view.listeners.TileClickListener;

public class TileDestinationFloorLine extends TileDestination implements TileAcceptor {

  public TileDestinationFloorLine(int cell, int row,
      TileClickListener tileClickListener,
      ModelTile modelTile) {
    super(cell, row, tileClickListener, modelTile);
  }

  @Override
  public void acceptClick(OnClickVisitor visitor) {
    visitor.visitOnClick(this);
  }
}
