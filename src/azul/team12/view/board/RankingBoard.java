package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Player;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * The Ranking board which shows the name of all players and their points in descending order.
 */

public class RankingBoard extends JPanel {
  private transient List<String> playerNamesList;
  private static final String GAME_RANKING = "RANKING";
  private static final int COL = 1;
  private static final long serialVersionUID = 7L;
  private transient final Controller controller;
  private transient JPanel rankingBoardPanel;
  private transient List<Player> playersList;

  /**
   * The constructor to create the ranking board showing the points of the players.
   *
   * @param controller controller used to combine the model with view.
   */
  public RankingBoard(Controller controller) {
    this.controller = controller;
    this.playerNamesList = controller.rankingPlayerWithPoints();

    createRankingArea();

  }

  /**
   * create the ranking area with the players' name and their points.
   */
  private void createRankingArea() {
    rankingBoardPanel = new JPanel();
    Border border = BorderFactory.createTitledBorder(GAME_RANKING);

    rankingBoardPanel.setBorder(border);

    rankingBoardPanel.setMaximumSize(new Dimension(600, 300));
    rankingBoardPanel.setLayout(new GridLayout(playerNamesList.size(), COL));
    add(rankingBoardPanel, BorderLayout.CENTER);
    for (String playerName : playerNamesList) {
        rankingBoardPanel.add(new JLabel(playerName + ": " + controller.getPoints(playerName)));
    }
  }

  /**
   * Updates the current ranking status of all players.
   */
  public void updateRankingBoard() {
    remove(rankingBoardPanel);
    playerNamesList = controller.rankingPlayerWithPoints();
    createRankingArea();
  }


}
