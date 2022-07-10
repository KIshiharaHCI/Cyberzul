package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;

public class ActivePlayerBoard extends PlayerBoard {
    private JButton forfeitButton, cancelGameButton, restartGameButton;
    private Dimension panelDimension;

    /**
     * The constructor to create a playerboard for a given player.
     *
     * @param controller
     * @param tileClickListener
     * @param playerName
     */
    public ActivePlayerBoard(Controller controller, TileClickListener tileClickListener,
                             String playerName, Dimension panelDimension) {
        super(controller, tileClickListener, playerName, Tile.NORMAL_TILE_SIZE);
        this.panelDimension = panelDimension;
        setActivePlayerBoardSize();
        initializeButtons();
    }

    /**
     * Calculates the panelSize for "this" based on the parent Panel Center Board.
     */
    private void setActivePlayerBoardSize() {
        panelDimension = new Dimension(
                (int) (panelDimension.width * 0.87),
                (int) (panelDimension.height * 0.55)
        );
        setMaximumSize(panelDimension);
        setMinimumSize(panelDimension);
    }

    private void initializeButtons() {
        forfeitButton = new JButton("FORFEIT");
        forfeitButton.setPreferredSize(new Dimension(25,10));
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
