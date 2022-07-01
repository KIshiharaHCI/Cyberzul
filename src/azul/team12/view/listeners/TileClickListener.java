package azul.team12.view.listeners;

import azul.team12.controller.Controller;
import azul.team12.model.GameModel;
import azul.team12.model.Offering;
import azul.team12.view.board.Tile;
import azul.team12.view.board.TileDestination;
import azul.team12.view.board.TileDestinationWall;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

/**
 * listens to what tile is clicked on and //TODO: makes the model change accordingly
 */
public class TileClickListener extends MouseAdapter implements ISourceTileListener,
    IDestinationTileListener, IDestinationWallTileListener {

  Tile source = null;
  TileDestination destination = null;
  private Controller controller;
  private GameModel model;

  public TileClickListener(Controller controller, GameModel model) {
    this.controller = controller;
    this.model = model;

  }

  /**
   * Depending on the class that has been clicked on
   *  - source tile (on manufacturing displays or table center --> create red border=
   *  - destination tile (pattern lines) --> place tile here
   *  - destination tile (wall) --> place tile here
   * @param e the event to be processed
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    Component source = e.getComponent();
    if (source instanceof Tile) {
      onSourceTileClick(((Tile) source));
    } else if (source instanceof TileDestination) {
      TileDestination destinationTile = (TileDestination) source;
      onDestinationTileClick(destinationTile);
    }
    // TODO: delete this part, should be done automatically via the model
    else if (source instanceof TileDestinationWall) {
      TileDestinationWall destinationWallTile = (TileDestinationWall) source;
      onDestinationWallTileClick(destinationWallTile);
    }

  }

  /**
   * create red border around tile if source tile was clicked
   */
  @Override
  public void onSourceTileClick(Tile tile) {
    System.out.println("Source was clicked with id " + tile.getTileId());
    source = tile;
    source.setBorder(BorderFactory.createLineBorder(Color.RED));
    List<Offering> factoryDisplays = controller.getFactoryDisplays();
    controller.chooseTileFrom(model.getNickOfActivePlayer(), source.getTileId(), factoryDisplays.get(source.getPlateId()));

  }

  /**
   * place tile of the respective color if destination tile on pattern line was clicked
   *
   * @param tileDestination - the source of the event if it is a destination tile
   */
  @Override
  public void onDestinationTileClick(TileDestination tileDestination) {
    System.out.println(
        "Destination was clicked with cell " + tileDestination.getCell() + " and row "
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

  //TODO: delete this method, should be done automatically by the model
  @Override
  public void onDestinationWallTileClick(TileDestinationWall destinationWall) {
    System.out.println(
        "Destination Wall was clicked with cell " + destinationWall.getCell() + " and row "
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
