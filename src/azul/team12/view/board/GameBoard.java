package azul.team12.view.board;

import azul.team12.model.GameModel;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * The board that shows the player boards of all (2 to 4) players. It also shows the table center
 * and the factory displays.
 */
public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final CenterBoard center;
  private final int numberOfPlayers;

  public static final int MAX_NUMBERS_OF_PLAYERS = 4;
  public static final int MIN_NUMBERS_OF_PLAYERS = 2;

  private GameModel model;

  private JPanel othersPanel;

  public GameBoard(final int numberOfPlayers, TileClickListener tileClickListener) {
    assert isValidNumberOfPlayers(numberOfPlayers);

    this.numberOfPlayers = numberOfPlayers;

    setLayout(new BorderLayout());
    setBackground(Color.lightGray);
    createLeft();

    center = new CenterBoard(numberOfPlayers, tileClickListener);
    add(center, BorderLayout.CENTER);
  }

  /**
   * Checks if the number of players is
   *
   * @param numberOfPlayers
   * @return
   */
  private static boolean isValidNumberOfPlayers(int numberOfPlayers) {
    model.
  }

  private void createLeft() {
    othersPanel = new JPanel();
    othersPanel.setMaximumSize(new Dimension(300, 300));
    othersPanel.setPreferredSize(new Dimension(300, 300));
    othersPanel.setLayout(new GridLayout(numberOfPlayers - 1, 1));
    for (int i = 0; i < numberOfPlayers - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard();
      othersPanel.add(playerBoard);
    }
    add(othersPanel, BorderLayout.WEST);
  }


}
