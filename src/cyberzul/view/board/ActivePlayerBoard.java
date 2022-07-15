package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Dimension;
import javax.swing.JButton;

public class ActivePlayerBoard extends PlayerBoard {

  private static final long serialVersionUID = 15L;
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
      controller.replacePlayerByAI(controller.getNickOfActivePlayer());
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
