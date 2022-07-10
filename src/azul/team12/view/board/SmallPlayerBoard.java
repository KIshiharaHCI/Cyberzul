package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;

public class SmallPlayerBoard extends PlayerBoard {

    /**
     * The constructor to create a playerboard for a given player.
     *
     * @param controller
     * @param tileClickListener
     * @param playerName
     */
    public SmallPlayerBoard(Controller controller, TileClickListener tileClickListener, String playerName) {
        super(controller, tileClickListener, playerName,Tile.SMALL_TILE_SIZE);
    }
}
