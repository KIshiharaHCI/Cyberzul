package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;

public class ActivePlayerBoard extends PlayerBoard {
    private JButton forfeitButton, cancelGameButton, restartGameButton;

    /**
     * The constructor to create a playerboard for a given player.
     *
     * @param controller
     * @param tileClickListener
     * @param playerName
     */
    public ActivePlayerBoard(Controller controller, TileClickListener tileClickListener,
                             String playerName, Dimension panelDimension) {
        super(controller, tileClickListener, playerName, Tile.NORMAL_TILE_SIZE, panelDimension);
        setMaximumSize(panelDimension);
        setMinimumSize(panelDimension);
    }
}
