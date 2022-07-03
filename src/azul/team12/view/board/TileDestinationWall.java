package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Subclass of TileDestination. Saves the information about each
 * Tile such as the TileclickListener, ImageIcons of Tiles, column and row.
 */
public class TileDestinationWall extends TileDestination {
  public TileDestinationWall(int cell, int row, int cellSize, TileClickListener tileClickListener,
                             ModelTile modelTile) {
    super(cell, row, cellSize, tileClickListener,modelTile);
  }
}

