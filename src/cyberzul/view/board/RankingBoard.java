package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.ImagePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** The Ranking board which shows the name of all players and their points in descending order. */
public class RankingBoard extends JPanel {

  private static final String GAME_RANKING = "RANKING";
  private static final int COL = 1;
  private static final long serialVersionUID = 7L;
  private final transient Controller controller;
  private final String RANKING_BOARD_PATH = "img/ranking-board.png";
  private transient List<String> playerNamesList;
  private transient JPanel rankingBoardPanel;

  /**
   * The constructor to create the ranking board showing the points of the players.
   *
   * @param controller controller used to combine the model with view.
   */
  public RankingBoard(Controller controller) {
    this.controller = controller;
    this.playerNamesList = controller.rankingPlayerWithPoints();
    setOpaque(false);
    createRankingArea();
  }

  /** create the ranking area with the players' name and their points. */
  private void createRankingArea() {
    rankingBoardPanel = new JPanel();
    rankingBoardPanel.setLayout(new GridLayout(playerNamesList.size(), COL));
    add(rankingBoardPanel, BorderLayout.CENTER);
    for (String player : playerNamesList) {
      // JTextField text = new JTextField();
      JLabel label = new JLabel();
      // Change text font size
      label.setFont(new Font("Serif", Font.BOLD, 20));
      // Change text font color
      label.setForeground(Color.green);
      label.setText("   " + player + ": " + controller.getPoints(player));
      rankingBoardPanel.add(label);
    }
    JPanel rankingBackgroundPanel =
        new ImagePanel(rankingBoardPanel, RANKING_BOARD_PATH, 200, 180, 1);
    add(rankingBackgroundPanel, rankingBoardPanel);
  }

  // new JLabel playerRankLabel = new IconButton()

  /** Updates the current ranking status of all players. */
  public void updateRankingBoard() {
    remove(rankingBoardPanel);
    playerNamesList = controller.rankingPlayerWithPoints();
    createRankingArea();
  }
}
