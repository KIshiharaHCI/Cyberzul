package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Dimension;

/**
 * Superclass of TileDestinationPatternLines and TileDestinationWall. Saves the information about
 * each Tile such as the TileclickListener, ImageIcons of Tiles, column and row.
 */
public class TileDestination extends Tile {


  private final int cell;
  private final int row;


  public TileDestination(int cell, int row, TileClickListener tileClickListener,
      ModelTile modelTile) {
    super(modelTile, tileClickListener);
    this.cell = cell;
    this.row = row;

    setAllSizes();

    addMouseListener(tileClickListener);
  }

  public TileDestination(int cell, int row, ModelTile modelTile) {
    super(modelTile);
    this.cell = cell;
    this.row = row;

    setAllSizes();

  }

  private void setAllSizes() {
    setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
    setMaximumSize(new Dimension(TILE_SIZE, TILE_SIZE));
    setMinimumSize(new Dimension(TILE_SIZE, TILE_SIZE));
  }


  public int getCell() {
    return cell;
  }

  public int getRow() {
    return row;
  }

}
