package azul.team12.view.listeners;

import azul.team12.view.board.Tile;
import azul.team12.view.board.TileDestination;
import azul.team12.view.board.TileDestinationWall;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class TileClickListener extends MouseAdapter implements ISourceTileListener,
    IDestinationTileListener, IDestinationWallTileListener {

  Tile source = null;
  TileDestination destination = null;

  @Override
  public void mouseClicked(MouseEvent e) {
    Component source = e.getComponent();
    if (source instanceof Tile) {

      onSourceTileClick(((Tile) source));
    } else if (source instanceof TileDestination) {
      TileDestination destinationTile = (TileDestination) source;
      onDestinationTileClick(destinationTile);
    } else if (source instanceof TileDestinationWall) {
      TileDestinationWall destinationWallTile = (TileDestinationWall) source;
      onDestinationWallTileClick(destinationWallTile);
    }

  }

  @Override
  public void onSourceTileClick(Tile tile) {
    System.out.println("Source was klicked with id " + tile.getId());
    source = tile;
    source.setBorder(BorderFactory.createLineBorder(Color.RED));

  }


  @Override
  public void onDestinationTileClick(TileDestination tileDestination) {
    System.out.println(
        "Destination was klicked with cell " + tileDestination.getCell() + " and row "
            + tileDestination.getRow());
    if (source != null) {
      ImageIcon icon = source.getIcon();
      tileDestination.setIcon(icon);
      tileDestination.getLabel().setIcon(icon);
      source.getLabel().setIcon(null);
      source.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      source = null;
    } else {
      destination = tileDestination;
    }
  }

  @Override
  public void onDestinationWallTileClick(TileDestinationWall destinationWall) {
    System.out.println(
        "Destination Wall was klicked with cell " + destinationWall.getCell() + " and row "
            + destinationWall.getRow());
    if (destination != null) {
      ImageIcon icon = destination.getIcon();
      destinationWall.setIcon(icon);
      destinationWall.getLabel().setIcon(icon);
      destination.getLabel().setIcon(null);
      destination = null;
    }
  }
}
