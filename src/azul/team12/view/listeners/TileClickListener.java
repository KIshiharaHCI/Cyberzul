package azul.team12.view.listeners;

import azul.team12.controller.Controller;
import azul.team12.model.GameModel;
import azul.team12.model.Offering;
import azul.team12.model.TableCenter;
import azul.team12.view.board.CenterBoard;
import azul.team12.view.board.PatternLines;
import azul.team12.view.board.Plate;
import azul.team12.view.board.PlatesPanel;
import azul.team12.view.board.TableCenterPanel;
import azul.team12.view.board.Tile;
import azul.team12.view.board.TileAcceptor;
import azul.team12.view.board.TileDestination;
import azul.team12.view.board.TileDestinationFloorLine;
import azul.team12.view.board.TileDestinationPatternLines;
import azul.team12.view.board.TileDestinationWall;
import azul.team12.view.board.TileSource;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

/**
 * listens to what tile is clicked on and //TODO: makes the model change accordingly
 */
public class TileClickListener extends MouseAdapter implements OnClickVisitor {

  TileSource source = null;
  TileDestination destination = null;
  private Controller controller;
  private GameModel model;

  public TileClickListener(Controller controller, GameModel model) {
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
   * place tile of the respective color if destination tile on pattern line was clicked
   *
   * @param tileDestination - the source of the event if it is a destination tile
   */
  public void onDestinationTileClick(TileDestinationPatternLines tileDestination) {
    System.out.println(
        "Destination was clicked with cell " + tileDestination.getCell() + " and row "
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
          platesPanel.initialize(controller.getFactoryDisplays(), this);

          CenterBoard centerBoard = (CenterBoard) platesPanel.getParent();
          TableCenterPanel tableCenterPanel = centerBoard.getTableCenterPanel();
          tableCenterPanel.remove();
          tableCenterPanel.initialize(this, (TableCenter) controller.getTableCenter());
        } else if (source.getPlateId() == 0) {
          TableCenterPanel tableCenterPanel = (TableCenterPanel) source.getParent().getParent();
          tableCenterPanel.remove();
          tableCenterPanel.initialize(this, (TableCenter) controller.getTableCenter());
        }
        //TODO: do it with a button on the playboard
        showSuccessMessage("Now it is " + controller.getNickOfNextPlayer() + "s turn!");
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

  @Override
  public void visitOnClick(TileDestinationPatternLines patternLinesTile) {
    onDestinationTileClick(patternLinesTile);
  }

  /**
   * create red border around tile if source tile was clicked
   */

  @Override
  public void visitOnClick(TileSource tileSource) {
    source = tileSource;
    source.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    // offerings, not factoryDisplays, because the first factory displays has id one not zero
    List<Offering> offerings = controller.getOfferings();
    controller.chooseTileFrom(model.getNickOfActivePlayer(), source.getTileId(),
        offerings.get(source.getPlateId()));

  }

  @Override
  public void visitOnClick(TileDestinationFloorLine floorLineTile) {
    controller.placeTileAtFloorLine();
    // draw newly floor line
    controller.endTurn(controller.getNickOfActivePlayer());
  }

  @Override
  public void visitOnClick(TileDestinationWall tileDestinationWall) {
    //onDestinationWallTileClick(tileDestinationWall);
  }
}
