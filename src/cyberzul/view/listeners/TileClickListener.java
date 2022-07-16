package cyberzul.view.listeners;

import cyberzul.controller.Controller;
import cyberzul.model.Model;
import cyberzul.model.Offering;
import cyberzul.model.TableCenter;
import cyberzul.view.board.ActivePlayerBoard;
import cyberzul.view.board.CenterBoard;
import cyberzul.view.board.DestinationTile;
import cyberzul.view.board.FloorLinePanel;
import cyberzul.view.board.PatternLines;
import cyberzul.view.board.Plate;
import cyberzul.view.board.PlatesPanel;
import cyberzul.view.board.PlayerBoard;
import cyberzul.view.board.SourceTile;
import cyberzul.view.board.TableCenterPanel;
import cyberzul.view.board.Tile;
import cyberzul.view.board.TileAcceptor;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

/** listens to what tile is clicked on and //TODO: makes the model change accordingly */
public class TileClickListener extends MouseAdapter implements OnClickVisitor {

  private final Controller controller;
  private final Model model;
  SourceTile source = null;
  DestinationTile destination = null;

  public TileClickListener(Controller controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  /**
   * Depending on the class that has been clicked on - source tile (on manufacturing displays or
   * table center --> create red border= - destination tile (pattern lines) --> place tile here -
   * destination tile (wall) --> place tile here
   *
   * @param e the event to be processed
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    Component tile = e.getComponent();
    if (tile instanceof TileAcceptor) {
      ((TileAcceptor) tile).acceptClick(this);
    }
  }

  /**
   * Select the {@link Tile} from an {@link Offering}.
   *
   * @param sourceTile: The {@link Tile} klicked on.
   */
  @Override
  public void visitOnClick(SourceTile sourceTile) {
    // second time click on the selected tile => unselect the tile
    if (sourceTile.equals(source)) {
      source.setBorder(BorderFactory.createEmptyBorder());
      source = null;
      return;
      // another tile selected after selecting one tile => unselect the first one
    } else if (source != null) {
      source.setBorder(BorderFactory.createEmptyBorder());
    }
    // select the current tile
    source = sourceTile;
    source.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    // offerings, not factoryDisplays, because the first factory displays has id one not zero
    List<Offering> offerings = controller.getOfferings();
    controller.chooseTileFrom(
        model.getNickOfActivePlayer(), source.getTileId(), source.getPlateId());
  }

  @Override
  public void visitOnClick(DestinationTile destinationTile) {
    onDestinationTileClick(destinationTile);
  }

  /**
   * place tile of the respective color if destination tile on pattern line was clicked
   *
   * @param tileDestination - the source of the event if it is a destination tile
   */
  public void onDestinationTileClick(DestinationTile tileDestination) {

    System.out.println(
        "Destination was clicked with cell "
            + tileDestination.getColumn()
            + " and row "
            + tileDestination.getRow());
    if (source != null) {
      // if the player is able to place the tile, place it
      if (tileDestination.getParent().getParent() instanceof FloorLinePanel) {
        controller.placeTileAtFloorLine();

        FloorLinePanel floorLinePanel = (FloorLinePanel) tileDestination.getParent().getParent();
        floorLinePanel.updateBottomTilesRow(controller.getNickOfActivePlayer());

        resetOffering();
        // showSuccessMessage("Now it is " + controller.getNickOfNextPlayer() + "s turn!");
        source = null;
      } else {
        controller.placeTileAtPatternLine(tileDestination.getRow());
        if (tileDestination.getParent().getParent() instanceof PatternLines) {
          PatternLines patternLinesView = (PatternLines) tileDestination.getParent().getParent();
          patternLinesView.remove();
          patternLinesView.initialize(
              patternLinesView.getTileSize(), controller.getNickOfActivePlayer(), this);
          PlayerBoard playerBoard =
              (ActivePlayerBoard) patternLinesView.getParent().getParent().getParent();
          playerBoard.getFloorLinePanel().updateBottomTilesRow(controller.getNickOfActivePlayer());
        }
        resetOffering();
        // TODO: do it with a button on the playboard
        // showSuccessMessage("Now it is " + controller.getNickOfNextPlayer() + "s turn!");
        source = null;
      }

    } else {
      destination = tileDestination;
    }
  }

  private void resetOffering() {
    source.setBorder(BorderFactory.createEmptyBorder());
    if (source.getPlateId() > 0) {
      Plate plate = (Plate) source.getParent().getParent();
      PlatesPanel platesPanel = (PlatesPanel) plate.getParent();
      platesPanel.remove();
      platesPanel.initialize(
          controller.getOfferings().subList(1, controller.getOfferings().size()), this);

      CenterBoard centerBoard = (CenterBoard) platesPanel.getParent().getParent().getParent();
      TableCenterPanel tableCenterPanel = centerBoard.getTableCenterPanel();
      tableCenterPanel.remove();
      tableCenterPanel.initialize(this, (TableCenter) controller.getOfferings().get(0));
    } else if (source.getPlateId() == 0) {
      TableCenterPanel tableCenterPanel = (TableCenterPanel) source.getParent().getParent();
      tableCenterPanel.remove();
      tableCenterPanel.initialize(this, (TableCenter) controller.getOfferings().get(0));
    }
  }

  /**
   * Show a success message as pop-up window to inform the user of an error.
   *
   * @param message the message with information about the error.
   */
  private void showSuccessMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Yeah!", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Show an error message as pop-up window to inform the user of an error.
   *
   * @param message the message with information about the error.
   */
  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
  }
}
