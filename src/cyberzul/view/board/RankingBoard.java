package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.ImagePanel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
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
  private final transient Controller controller;
  private static final String rankingBoardPath = "img/ranking-board.png";
  private static final String gameFontPath = "fonts/gameOfSquids.ttf";
  private transient List<String> playerNamesList;
  private transient JPanel rankingBackgroundPanel;


  /**
   * The constructor to create the ranking board showing the points of the players.
   *
   * @param controller controller used to combine the model with view.
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  //this class needs these references to these mutable objects.
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
    Font sizedFont;
    JPanel rankingBoardPanel = new JPanel();
    rankingBoardPanel.setBorder(new EmptyBorder(10, 5, 10, 5));
    rankingBoardPanel.setLayout(new GridLayout(playerNamesList.size(), COL));
    add(rankingBoardPanel, BorderLayout.CENTER);

    try {
    InputStream is = getClass().getClassLoader().getResourceAsStream(gameFontPath);
      assert is != null;
      Font gameFont = Font.createFont(Font.TRUETYPE_FONT, is);
    sizedFont = gameFont.deriveFont(18f);
    } catch (IOException | FontFormatException ex) {
      throw new IllegalStateException(ex);
    }

    for (String player : playerNamesList) {
      JLabel label = new JLabel();
      label.setFont(sizedFont);
      label.setForeground(Color.white);
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setText(player + ": " + controller.getPoints(player));
      rankingBoardPanel.add(label);
    }
    rankingBackgroundPanel =
        new ImagePanel(rankingBoardPanel, rankingBoardPath, 200, 220, 1);
    add(rankingBackgroundPanel, rankingBoardPanel);
  }


  /**
   * Updates the current ranking status of all players.
   */
  public void updateRankingBoard(){
    remove(rankingBackgroundPanel);
    playerNamesList = controller.rankingPlayerWithPoints();
    createRankingArea();
  }

}
