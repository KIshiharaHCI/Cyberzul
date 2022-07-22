package cyberzul.view.board;

import static cyberzul.view.CyberzulView.getCustomFont;

import cyberzul.controller.Controller;
import cyberzul.view.ImagePanel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.Serial;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * The Ranking board which shows the name of all players and their points in descending order.
 */
public class RankingBoard extends JPanel {

  private static final int COL = 1;
  @Serial
  private static final long serialVersionUID = 7L;
  private static final String rankingBoardPath = "img/ranking-board.png";
  private final transient Controller controller;
  private transient List<String> playerNamesList;
  private transient JPanel rankingBackgroundPanel;

  /**
   * The constructor to create the ranking board showing the points of the players.
   *
   * @param controller controller used to combine the model with view.
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  // this class needs these references to these mutable objects.
  public RankingBoard(Controller controller) {
    this.controller = controller;
    this.playerNamesList = controller.rankingPlayerWithPoints();
    setOpaque(false);
    createRankingArea();
  }

  /**
   * create the ranking area with the players' name and their points.
   */
  private void createRankingArea() {
    JPanel rankingBoardPanel = new JPanel();
    rankingBoardPanel.setBorder(new EmptyBorder(10, 5, 10, 5));
    rankingBoardPanel.setLayout(new GridLayout(playerNamesList.size(), COL));
    add(rankingBoardPanel, BorderLayout.CENTER);

    for (String player : playerNamesList) {
      JLabel label = new JLabel();
      label.setFont(getCustomFont().deriveFont(18f));
      label.setForeground(Color.white);
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setText(player + ": " + controller.getPoints(player));
      rankingBoardPanel.add(label);
    }
    rankingBackgroundPanel = new ImagePanel(rankingBoardPanel, rankingBoardPath, 200, 220, 1);
    add(rankingBackgroundPanel, rankingBoardPanel);
  }

  /**
   * Updates the current ranking status of all players.
   */
  public void updateRankingBoard() {
    remove(rankingBackgroundPanel);
    playerNamesList = controller.rankingPlayerWithPoints();
    createRankingArea();
  }
}
