package azul.team12.view.listeners;

import azul.team12.view.board.Tile;
import azul.team12.view.board.TileCenter;
import azul.team12.view.board.TileDestination;
import azul.team12.view.board.TileDestinationWall;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class TileClickListener extends MouseAdapter implements ISourceTileListener,
    ISourceCenterTileListener, IDestinationTileListener, IDestinationWallTileListener {

  Tile source = null;
  TileCenter sourceCenter = null;
  TileDestination destination = null;

  @Override
  public void mouseClicked(MouseEvent e) {
    Component component = e.getComponent();
    if (component instanceof Tile) {
      onSourceTileClick(((Tile) component));
    } else if (component instanceof TileCenter) {
      onSourceTileCenterClick((TileCenter) component);
    } else if (component instanceof TileDestination) {
      TileDestination destinationTile = (TileDestination) component;
      onDestinationTileClick(destinationTile);
    } else if (component instanceof TileDestinationWall) {
      TileDestinationWall destinationWallTile = (TileDestinationWall) component;
      onDestinationWallTileClick(destinationWallTile);
    }

  }

  @Override
  public void onSourceTileClick(Tile tile) {
    System.out.println("Source was klicked with id " + tile.getId());
    if (tile.getIcon() == null) {
      return;
    }
    if (sourceCenter != null) {
      sourceCenter.setBorder(BorderFactory.createEmptyBorder());

      sourceCenter = null;
    }
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
      source.setIcon(null);
      source.setOpaque(false);
      source.getLabel().setVisible(false);
      source.setBorder(BorderFactory.createEmptyBorder());
      source = null;
    } else {
      if (tileDestination.getIcon() == null) {
        return;
      }
      destination = tileDestination;
      if (destination.getIcon() != null) {
        destination.setBorder(BorderFactory.createLineBorder(Color.RED));
      }
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
      destination.setIcon(null);
      destination.setBorder(BorderFactory.createEmptyBorder());
      destination = null;
    }
  }

  @Override
  public void onSourceTileCenterClick(TileCenter tileCenter) {
    if (tileCenter.getIcon() == null) {
      return;
    }
    if (source != null) {
      source.setBorder(BorderFactory.createEmptyBorder());
      source = null;
    }
    sourceCenter = tileCenter;

    sourceCenter.setBorder(BorderFactory.createLineBorder(Color.RED));

  }
}
