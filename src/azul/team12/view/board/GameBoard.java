package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The board that shows the player boards of all (2 to 4) players. It also shows the table center
 * and the factory displays.
 */
public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private transient final Controller controller;

  private final CenterBoard center;
  private final int numberOfPlayers;
  private transient List<Offering> factoryDisplays;

  private JPanel boardsOfOpponentsPanel;

  private JPanel rankingBoardPanel;
  private JPanel settingsPanel = new JPanel(null);

  private RankingBoard rankingBoard;
  private Dimension frameDimension;


  public GameBoard(final int numberOfPlayers, TileClickListener tileClickListener,
      Controller controller, Dimension frameDimension) {

    this.controller = controller;
    this.numberOfPlayers = numberOfPlayers;
    factoryDisplays = controller.getOfferings().subList(1, controller.getOfferings().size());

    this.frameDimension = frameDimension;
    setLayout(new BorderLayout());
    setBackground(Color.lightGray);
    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);

    createOpponentsPanel();
    createChatPanel();

    center = new CenterBoard(controller, tileClickListener,frameDimension);
    center.setBackground(Color.red);
    add(center, BorderLayout.CENTER);

    //createRankingBoardPanel();

  }

  private void createChatPanel() {
    //TODO: replace temporary chatPanel with Iurii's Chat class, remove Background
    JPanel chatPanel = new JPanel();
    Dimension chatPanelDimension = new Dimension(
            (int) (frameDimension.width * 0.3),
            (int) (frameDimension.height * 0.94)
    );
    chatPanel.setMinimumSize(chatPanelDimension);
    chatPanel.setMaximumSize(chatPanelDimension);
    chatPanel.setBackground(Color.blue);
    add(chatPanel,BorderLayout.EAST);
  }

  private void createRankingBoardPanel() {
    rankingBoardPanel = new JPanel();
    rankingBoard = new RankingBoard(controller);
    rankingBoardPanel.add(rankingBoard);
    add(rankingBoardPanel, BorderLayout.EAST);
  }


  private void createOpponentsPanel() {
    boardsOfOpponentsPanel = new JPanel();
    Dimension opponentsPanelDimension = new Dimension(
            (int) (frameDimension.width * 0.2),
            (int) (frameDimension.height * 0.94)
    );
    boardsOfOpponentsPanel.setMaximumSize(opponentsPanelDimension);
    boardsOfOpponentsPanel.setPreferredSize(opponentsPanelDimension);
    boardsOfOpponentsPanel.setLayout(new GridLayout(3, 1));

    List<String> listOfActivePlayers = controller.getPlayerNamesList();
    Dimension playerBoardDimension = new Dimension(frameDimension.width - 20, 200);
    for (String opponentPlayer : listOfActivePlayers) {
      if (!opponentPlayer.equals(controller.getNickOfActivePlayer())) {
        PlayerBoard playerBoard = new SmallPlayerBoard(controller, null,
                opponentPlayer,playerBoardDimension);
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

  /**
   * Update the Points and oder players according to their points in the descending order when the round ends.
   */
  public void updateRankingBoard() {
    rankingBoard.updateRankingBoard();
    validate();

  }
}
