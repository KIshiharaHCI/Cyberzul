package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;

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


  private JPanel settingsPanel = new JPanel(null);

  private RankingBoard rankingBoard;
  private Dimension frameDimension;
  private List<PlayerBoard> otherPlayerBoards;


  public GameBoard(final int numberOfPlayers, TileClickListener tileClickListener,
      Controller controller, Dimension frameDimension) {

    this.controller = controller;
    this.numberOfPlayers = numberOfPlayers;
    factoryDisplays = controller.getOfferings().subList(1, controller.getOfferings().size());

    this.frameDimension = frameDimension;
    setLayout(new BorderLayout());
    //setBackground(Color.lightGray);
    setOpaque(false);
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
    ChatPanel chatPanel = new ChatPanel();
    Dimension chatPanelDimension = new Dimension(
            (int) (frameDimension.width * 0.3),
            (int) (frameDimension.height * 0.94)
    );
    chatPanel.setMinimumSize(chatPanelDimension);
    chatPanel.setMaximumSize(chatPanelDimension);
    chatPanel.setPreferredSize(chatPanelDimension);
    chatPanel.setBackground(Color.blue);
    add(chatPanel,BorderLayout.EAST);
  }

  private void createOpponentsPanel() {
    otherPlayerBoards = new ArrayList<>();
    boardsOfOpponentsPanel = new JPanel();
    boardsOfOpponentsPanel.setOpaque(false);
    Dimension opponentsPanelDimension = new Dimension(
            (int) (frameDimension.width * 0.25),
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
        otherPlayerBoards.add(playerBoard);
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
   * Update the Points and oder players according to their points in the descending order when the
   * round ends.
   */
  public void updateRankingBoard() {
    center.updateRankingBoard();
    validate();

  }
  public void updateOtherPlayerBoards() {

    for (PlayerBoard othersPlayerBoard : otherPlayerBoards) {
      boardsOfOpponentsPanel.remove(othersPlayerBoard);
    }
    otherPlayerBoards.clear();
    List<String> listOfActivePlayers = controller.getPlayerNamesList();
    String nameOfCurrentPlayer = controller.getNickOfActivePlayer();
    int indexOfCurrentPlayer = listOfActivePlayers.indexOf(nameOfCurrentPlayer);
    Dimension playerBoardDimension = new Dimension(frameDimension.width - 20, 200);
    initializeOnePart(indexOfCurrentPlayer + 1, listOfActivePlayers.size(), listOfActivePlayers,
        playerBoardDimension);

    initializeOnePart(0, indexOfCurrentPlayer, listOfActivePlayers, playerBoardDimension);


  }

  private void initializeOnePart(int indexOfCurrentPlayer, int listOfActivePlayersSize,
      List<String> listOfActivePlayers, Dimension playerBoardDimension) {
    for (int i = indexOfCurrentPlayer; i < listOfActivePlayersSize; i++) {
      PlayerBoard playerBoard = new SmallPlayerBoard(controller, null,
          listOfActivePlayers.get(i), playerBoardDimension);
      boardsOfOpponentsPanel.add(playerBoard);
      otherPlayerBoards.add(playerBoard);

    }
  }

}
