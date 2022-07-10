package azul.team12.view.listeners;

import azul.team12.controller.Controller;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.Offering;
import azul.team12.model.TableCenter;
import azul.team12.view.board.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * listens to what tile is clicked on and //TODO: makes the model change accordingly
 */

public class TileClickListener extends MouseAdapter implements OnClickVisitor {

  SourceTile source = null;
  DestinationTile destination = null;
  private Controller controller;
  private Model model;

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
   * create red border around tile if source tile was clicked
   */
  @Override
  public void visitOnClick(SourceTile sourceTile) {
    source = sourceTile;
    source.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    // offerings, not factoryDisplays, because the first factory displays has id one not zero
    List<Offering> offerings = controller.getOfferings();
    controller.chooseTileFrom(model.getNickOfActivePlayer(), source.getTileId(), source.getPlateId());

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
        "Destination was clicked with cell " + tileDestination.getColumn() + " and row "
            + tileDestination.getRow());
    if (source != null) {
      // if the player is able to place the tile, place it
      if (controller.placeTileAtPatternLine(tileDestination.getRow())) {
        PatternLines patternLinesView = (PatternLines) tileDestination.getParent().getParent();
        patternLinesView.remove();
        patternLinesView.initialize(Tile.TILE_SIZE, this);

        source.setBorder(BorderFactory.createEmptyBorder());
        if (source.getPlateId() > 0) {
          Plate plate = (Plate) source.getParent().getParent();
          PlatesPanel platesPanel = (PlatesPanel) plate.getParent();
          platesPanel.remove();
          platesPanel.initialize(controller.getOfferings().subList(1,controller.getOfferings().size()), this);

          CenterBoard centerBoard = (CenterBoard) platesPanel.getParent();
          TableCenterPanel tableCenterPanel = centerBoard.getTableCenterPanel();
          tableCenterPanel.remove();
          tableCenterPanel.initialize(this, (TableCenter) controller.getOfferings().get(0));
        } else if (source.getPlateId() == 0) {
          TableCenterPanel tableCenterPanel = (TableCenterPanel) source.getParent().getParent();
          tableCenterPanel.remove();
          tableCenterPanel.initialize(this, (TableCenter) controller.getOfferings().get(0));
        }
        //TODO: do it with a button on the playboard
        //showSuccessMessage("Now it is " + controller.getNickOfNextPlayer() + "s turn!");
        controller.endTurn(source.getName());
        source = null;
      } else {
        showErrorMessage("Not a valid turn!");
      }

    } else {
      destination = tileDestination;
    }
  }

  /**
   * Show a success message as pop-up window to inform the user of an error.
   *
   * @param message the message with information about the error.
   */
  private void showSuccessMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Yeah!",
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Show an error message as pop-up window to inform the user of an error.
   *
   * @param message the message with information about the error.
   */
  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error!",
        JOptionPane.ERROR_MESSAGE);
  }

}
