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
        initializeButtons();
    }

    private void initializeButtons() {
        forfeitButton = new JButton("FORFEIT");
        forfeitButton.addActionListener(event -> {
            controller.replaceActivePlayerByAI();
            System.out.println("Forfeit Button has been clicked");
        });

        cancelGameButton = new JButton("CANCEL");
        cancelGameButton.addActionListener(event -> {
            controller.cancelGameForAllPlayers();
            System.out.println("Cancel Button has been pressed.");
        });

        restartGameButton = new JButton("RESTART");
        restartGameButton.addActionListener(event -> {
            controller.restartGame();
            System.out.println("Game has been restarted.");
        });
        north.add(forfeitButton);
        north.add(cancelGameButton);
        north.add(restartGameButton);
    }
}
