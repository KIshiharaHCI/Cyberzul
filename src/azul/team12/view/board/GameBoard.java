package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  //List<PlayerBoard> playerBoardList = new ArrayList<>();

  private final CenterBoard center;
  private final int numberOfPlayers;

  private JPanel othersPanel;

  public GameBoard(final int numberOfPlayers, TileClickListener tileClickListener) {
    assert isValidNumberOfPlayers(numberOfPlayers);

    this.numberOfPlayers = numberOfPlayers;

    //  setPreferredSize(new Dimension(width, height));
    setLayout(new BorderLayout());
    // setBackground(new Color(110,90,120));
    createLeft();

    center = new CenterBoard(numberOfPlayers, tileClickListener);
    add(center, BorderLayout.CENTER);
  }

  private static boolean isValidNumberOfPlayers(int numberOfPlayers) {
    return numberOfPlayers <= 4 && numberOfPlayers >= 2;
  }

  private void createLeft() {
    othersPanel = new JPanel();
    //othersPanel.setLayout(new BoxLayout(othersPanel, BoxLayout.Y_AXIS));
    othersPanel.setMaximumSize(new Dimension(300, 300));
    othersPanel.setPreferredSize(new Dimension(300, 300));
    othersPanel.setLayout(new GridLayout(numberOfPlayers - 1, 1));
    for (int i = 0; i < numberOfPlayers - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard();

      //playerBoardList.add(playerBoard);
      //othersPanel.add(playerBoard);
      othersPanel.add(playerBoard);
    }
    add(othersPanel, BorderLayout.WEST);
  }

  private String calculateNextPosition(String position) {
    if (position.equals(BorderLayout.NORTH)) {
      return BorderLayout.CENTER;
    } else if (position.equals(BorderLayout.CENTER)) {
      return BorderLayout.SOUTH;
    }
    return "";
  }


}
