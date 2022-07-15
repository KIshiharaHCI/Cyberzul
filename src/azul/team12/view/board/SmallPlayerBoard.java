package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Dimension;

public class SmallPlayerBoard extends PlayerBoard {


  private static final long serialVersionUID = 14L;

  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param controller
   * @param tileClickListener
   * @param playerName
   */
  public SmallPlayerBoard(Controller controller, TileClickListener tileClickListener,
      String playerName, Dimension panelDimension) {
    super(controller, tileClickListener, playerName, Tile.SMALL_TILE_SIZE, panelDimension);
    setOpaque(false);
    setSmallPlayerBoardSize(panelDimension);
  }

  private void setSmallPlayerBoardSize(Dimension panelDimension) {
    panelDimension = new Dimension((int) (panelDimension.width * 0.87), 200);
    setMaximumSize(panelDimension);
    setMinimumSize(panelDimension);
  }
}
