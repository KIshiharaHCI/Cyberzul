package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.IconButton;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The board that shows the player boards of all (2 to 4) players. It also shows the table center
 * and the factory displays.
 */
public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private transient final Controller controller;

  private final CenterBoard center;

  private JPanel boardsOfOpponentsPanel;


  private JPanel settingsPanel = new JPanel(null);

  private RankingBoard rankingBoard;
  private Dimension frameDimension;
  private List<PlayerBoard> otherPlayerBoards;
  private final String SOUND_BUTTON_PATH = "img/sound-button.png";
  private final String MENU_BUTTON_PATH = "img/settings-button.png";
  private final int ICON_BUTTON_SIZE = 40;

  /**
   * Creates the main game panel which contains all other game elements.
   * @param tileClickListener
   * @param controller
   * @param frameDimension 1400 x 800 px
   */
  public GameBoard(TileClickListener tileClickListener,
      Controller controller, Dimension frameDimension) {

    this.controller = controller;
    this.frameDimension = frameDimension;
    setLayout(new BorderLayout());
    setOpaque(false);
    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);

    createOpponentsPanel();
    createChatAndRankingBoardAndSettingPanel();

    center = new CenterBoard(controller, tileClickListener,frameDimension);
    add(center, BorderLayout.CENTER);

    //createRankingBoardPanel();

  }
  //TODO createChatAndRankingBoardAndButtonsPanel
  private void createChatAndRankingBoardAndSettingPanel() {
    //TODO: replace temporary chatPanel with Iurii's Chat class, remove Background

    // create the Panel with RankingBoard, SettingBoard and Chat.
    JPanel chatAndRankingBoardAndSettingPanel = new JPanel();
    chatAndRankingBoardAndSettingPanel.setLayout(new BorderLayout());
    chatAndRankingBoardAndSettingPanel.setOpaque(false);

    // create the Panel with RankingBoard and SettingBoard.
    JPanel rankingBoardAndSettingPanel = new JPanel();
    rankingBoardAndSettingPanel.setLayout(new GridLayout(1,2));
    rankingBoardAndSettingPanel.setOpaque(false);

    JPanel rankingBoardPanel = new JPanel();
    rankingBoard = new RankingBoard(controller);
    rankingBoardPanel.add(rankingBoard);
    rankingBoardAndSettingPanel.add(rankingBoard);

    JPanel settingsPanel = new JPanel();
    settingsPanel.setLayout(null);



    JButton soundButton = new IconButton(SOUND_BUTTON_PATH, 130, 20, ICON_BUTTON_SIZE, ICON_BUTTON_SIZE);
    settingsPanel.add(soundButton);

    JButton settingsButton = new IconButton(MENU_BUTTON_PATH, 130, 100, ICON_BUTTON_SIZE, ICON_BUTTON_SIZE);
    final JPopupMenu menu = new JPopupMenu("Menu");
    JMenuItem firstItem = new JMenuItem("First item");
    firstItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        System.out.println("First menu item clicked");
      }
    } );
    menu.add(firstItem);
    menu.add(new JMenuItem("Second item"));
    menu.add(new JMenuItem("Third item"));

    settingsButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        menu.show(settingsButton, -ICON_BUTTON_SIZE*2, ICON_BUTTON_SIZE/2);
      }
    } );
    settingsPanel.add(settingsButton);

    settingsPanel.setOpaque(false);
    rankingBoardAndSettingPanel.add(settingsPanel);

    chatAndRankingBoardAndSettingPanel.add(rankingBoardAndSettingPanel,BorderLayout.CENTER);

    ChatPanel chatPanel = new ChatPanel();
    chatAndRankingBoardAndSettingPanel.add(chatPanel, BorderLayout.SOUTH);


    Dimension chatAndRankingBoardAndButtonsPanelDimension = new Dimension(
            (int) (frameDimension.width * 0.3),
            (int) (frameDimension.height * 0.94)
    );
    chatAndRankingBoardAndSettingPanel.setMinimumSize(chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.setMaximumSize(chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.setPreferredSize(chatAndRankingBoardAndButtonsPanelDimension);

    add(chatAndRankingBoardAndSettingPanel,BorderLayout.EAST);
  }
//  private void createChatPanel() {
//    //TODO: replace temporary chatPanel with Iurii's Chat class, remove Background
//    ChatPanel chatPanel = new ChatPanel();
//    Dimension chatPanelDimension = new Dimension(
//        (int) (frameDimension.width * 0.3),
//        (int) (frameDimension.height * 0.94)
//    );
//    chatPanel.setMinimumSize(chatPanelDimension);
//    chatPanel.setMaximumSize(chatPanelDimension);
//    chatPanel.setPreferredSize(chatPanelDimension);
//
//    add(chatPanel,BorderLayout.EAST);
//  }

  /**
   * Creates the sidebar with the panels of the opponents.
   */
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
    rankingBoard.updateRankingBoard();
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
