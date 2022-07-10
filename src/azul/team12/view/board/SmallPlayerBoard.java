package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;

public class SmallPlayerBoard extends PlayerBoard {

    /**
     * The constructor to create a playerboard for a given player.
     *
     * @param controller
     * @param tileClickListener
     * @param playerName
     */
    public SmallPlayerBoard(Controller controller, TileClickListener tileClickListener,
                            String playerName, Dimension panelDimension) {
        super(controller, tileClickListener, playerName,Tile.SMALL_TILE_SIZE, panelDimension);
        setSmallPlayerBoardSize(panelDimension);
    }

    private void setSmallPlayerBoardSize(Dimension panelDimension) {
        panelDimension = new Dimension((int) (panelDimension.width * 0.87),200);
        setMaximumSize(panelDimension);
        setMinimumSize(panelDimension);
    }

    @Override
    JPanel setPlayerBoardWrapperSize() {
        JPanel playerBoardWrapper = new JPanel(new BorderLayout());
        Dimension wrapperDimension = new Dimension(
                (int) (panelDimension.width * 0.87), panelDimension.height = 200
        );
        playerBoardWrapper.setOpaque(false);
        return playerBoardWrapper;
    }
}
