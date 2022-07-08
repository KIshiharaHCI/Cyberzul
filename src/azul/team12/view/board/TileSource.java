package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.OnClickVisitor;
import azul.team12.view.listeners.TileClickListener;

public class TileSource extends Tile implements TileAcceptor {

  private final int tileId;

  private int plateId;

  /**
   * Creates a tile.
   * <p>
   * TODO: ID is a index?
   *  @param tileId
   *
   * @param plateId
   * @param modelTile
   * @param tileClickListener
   */
  public TileSource(int tileId, int plateId, ModelTile modelTile,
      TileClickListener tileClickListener) {
    super(modelTile, tileClickListener);
    this.tileId = tileId;
    this.plateId = plateId;
    setToolTipText(tileId + "");
  }

  public int getTileId() {
    return tileId;
  }

  public int getPlateId() {
    return plateId;
  }

  @Override
  public void acceptClick(OnClickVisitor visitor) {
    visitor.visitOnClick(this);
  }
}
