package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JPanel;

/**
 * The board that shows the player boards of all (2 to 4) players. It also shows the table center
 * and the factory displays.
 */
public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final Controller controller;

  private final CenterBoard center;
  private final int numberOfPlayers;
  private List<Offering> factoryDisplays;

  private JPanel boardsOfOpponentsPanel;

  public GameBoard(final int numberOfPlayers, TileClickListener tileClickListener,
      Controller controller) {

    this.controller = controller;
    this.numberOfPlayers = numberOfPlayers;
    factoryDisplays = controller.getFactoryDisplays();

    setLayout(new BorderLayout());
    setBackground(Color.lightGray);
    createPanelWithTheBoardsOfOpponents();

    center = new CenterBoard(controller, tileClickListener);
    add(center, BorderLayout.CENTER);
  }


  private void createPanelWithTheBoardsOfOpponents() {
    boardsOfOpponentsPanel = new JPanel();
    boardsOfOpponentsPanel.setMaximumSize(new Dimension(450, 500));
    boardsOfOpponentsPanel.setPreferredSize(new Dimension(450, 500));
    boardsOfOpponentsPanel.setLayout(new GridLayout(3, 1));

    List<String> listOfActivePlayers = controller.getPlayerNamesList();
    for (int i = 0; i < listOfActivePlayers.size(); i++) {
      String nameOfOpponent = controller.getNickOfActivePlayer();
      if (!nameOfOpponent.equals(listOfActivePlayers.get(i))) {
        // listener is null, because no click events should happen here.
        PlayerBoard playerBoard = new PlayerBoard(controller, null,
            nameOfOpponent);
        boardsOfOpponentsPanel.add(playerBoard);
      }
    }
    add(boardsOfOpponentsPanel, BorderLayout.WEST);
  }


  /**
   * Used by view to update all widgets in Center Board.
   */
  public void updateCenterBoard() {
    center.updateCenterBoard();
    validate();
  }
}
