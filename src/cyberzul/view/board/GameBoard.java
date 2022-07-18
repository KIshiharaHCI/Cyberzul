package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.IconButton;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
/**
 * The board that shows the player boards of all (2 to 4) players. It also shows the table center
 * and the factory displays.
 */
public class GameBoard extends JPanel {

  @Serial private static final long serialVersionUID = 7526472295622776147L;
  private static final String soundButtonPath = "img/sound-button.png";
  private static final String menuButtonPath = "img/settings-button.png";
  private static final int iconButtonSize = 40;
  private final transient Controller controller;
  private final CenterBoard center;
  private final Dimension frameDimension;
  private JPanel settingsPanel = new JPanel(null);
  private JPanel boardsOfOpponentsPanel;
  private RankingBoard rankingBoard;
  private transient List<PlayerBoard> otherPlayerBoards;
  private IconButton soundButton;
  private IconButton settingsButton;
  private JPanel menu;
  private JSlider musicSound;
  private JSlider systemSound;
  private IconButton forfeitButton;
  private IconButton cancelButton;
  private IconButton restartButton;
  private JPanel systemSoundPanel;
  private JLabel musicSoundLabel;

  /**
   * Creates the main game panel which contains all other game elements.
   *
   * @param tileClickListener the tile click listener
   * @param controller the controller
   * @param frameDimension 1400 x 800 px
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  // this class needs these references to these mutable objects.
  public GameBoard(
      TileClickListener tileClickListener, Controller controller, Dimension frameDimension) {

    this.controller = controller;
    this.frameDimension = frameDimension;
    setLayout(new BorderLayout());
    setOpaque(false);
    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);

    createOpponentsPanel();
    createChatAndRankingBoardAndSettingPanel();

    center = new CenterBoard(controller, tileClickListener, frameDimension);
    add(center, BorderLayout.CENTER);
  }

  private void createChatAndRankingBoardAndSettingPanel() {

    // create the Panel with RankingBoard, SettingBoard and Chat.
    JPanel chatAndRankingBoardAndSettingPanel = new JPanel();
    chatAndRankingBoardAndSettingPanel.setLayout(new BorderLayout());
    chatAndRankingBoardAndSettingPanel.setOpaque(false);
    Dimension chatAndRankingBoardAndButtonsPanelDimension =
        new Dimension((int) (frameDimension.width * 0.26), (int) (frameDimension.height * 0.94));
    chatAndRankingBoardAndSettingPanel.setMinimumSize(chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.setMaximumSize(chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.setPreferredSize(
        chatAndRankingBoardAndButtonsPanelDimension);

    add(chatAndRankingBoardAndSettingPanel, BorderLayout.EAST);

    // create the Panel with RankingBoard and SettingBoard.
    JPanel rankingBoardAndSettingPanel = new JPanel();
    rankingBoardAndSettingPanel.setLayout(new GridLayout(1, 2));
    rankingBoardAndSettingPanel.setOpaque(false);

    JPanel rankingBoardPanel = new JPanel();
    rankingBoard = new RankingBoard(controller);
    rankingBoardPanel.add(rankingBoard);
    rankingBoardAndSettingPanel.add(rankingBoard);
    createSettingsPanel();
    rankingBoardAndSettingPanel.add(settingsPanel);

    /** SettingPanel. */

    //    settingsPanel = new JPanel();
    //    settingsPanel.setLayout(null);
    //
    //
    //    settingsPanel.add(soundButton);

    //    final JPopupMenu menu = new JPopupMenu("Menu");
    //    JMenuItem firstItem = new JMenuItem("First item");
    //    firstItem.addActionListener(ae -> System.out.println("First menu item clicked"));
    //    menu.add(firstItem);
    //    menu.add(new JMenuItem("Second item"));
    //    menu.add(new JMenuItem("Third item"));

    //    settingsButton.addActionListener(
    //        ae -> menu.show(settingsButton, -iconButtonSize * 2, iconButtonSize / 2));
    //    settingsPanel.add(settingsButton);
    //
    //    settingsPanel.setOpaque(false);
    rankingBoardAndSettingPanel.add(settingsPanel);

    chatAndRankingBoardAndSettingPanel.add(rankingBoardAndSettingPanel, BorderLayout.NORTH);

    /** Place for timer. TODO timer */
    JLabel tempLabel = new JLabel();
    tempLabel.setHorizontalAlignment(JLabel.LEFT);
    tempLabel.setFont(new Font("Dialog", Font.BOLD, 30));
    tempLabel.setText("place for timer");
    chatAndRankingBoardAndSettingPanel.add(tempLabel, BorderLayout.CENTER);

    ChatPanel chatPanel = new ChatPanel();
    chatAndRankingBoardAndSettingPanel.add(chatPanel, BorderLayout.SOUTH);
  }

  /** Initialise all buttons for settingPanel. */
  private void initializeSettingWidgets() {

    soundButton =
        new IconButton(
            soundButtonPath, 10, 80, (int) (iconButtonSize * 0.95), (int) (iconButtonSize * 0.95));

    settingsButton =
        new IconButton(
            menuButtonPath, 10, 20, (int) (iconButtonSize * 0.95), (int) (iconButtonSize * 0.95));

    settingsButton.addActionListener(ae -> menu.setVisible(true));

    menu = new JPanel(new GridLayout(5, 1));
    menu.setPreferredSize(new Dimension(150, 300));
    menu.setOpaque(false);

    musicSoundLabel = new JLabel("music sound");
    musicSoundLabel.setSize(100, 18);

    musicSound = new JSlider();
    musicSound.setSize(100, 18);
    musicSound.setOpaque(false);
    // musicSoundLabel.add(musicSound);

    systemSound = new JSlider();
    systemSound.setSize(100, 30);
    systemSound.setOpaque(false);

    systemSoundPanel = new JPanel(new GridLayout(2, 1));
    //    ImagePanel systemSoundBackGroundPanel =
    //        new ImagePanel(systemSoundPanel, "img/tile-outline.png", 100,40,1);
    //    add(systemSoundBackGroundPanel, systemSoundPanel);
    systemSoundPanel.setOpaque(false);
    systemSoundPanel.setPreferredSize(new Dimension(105, 40));
    JLabel label = new JLabel();
    label.setSize(100, 18);
    // label.setFont(ne);
    label.setForeground(Color.white);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setText("system sound");
    // systemSoundPanel.add(musicSoundLabel);
    systemSoundPanel.add(systemSound);
    systemSoundPanel.add(label);

    forfeitButton = new IconButton("img/forfeit-button.png", 0, 0, 105, 39);
    cancelButton = new IconButton("img/cancel-button.png", 0, 0, 105, 39);
    restartButton = new IconButton("img/restart-button.png", 0, 0, 105, 39);
    // menu.add(musicSoundLabel);
    menu.add(musicSound);
    menu.add(systemSoundPanel);
    menu.add(forfeitButton);
    menu.add(cancelButton);
    menu.add(restartButton);
  }

  private void createSettingsPanel() {
    initializeSettingWidgets();
    settingsPanel = new JPanel(new BorderLayout());
    settingsPanel.setOpaque(false);
    settingsPanel.add(menu, BorderLayout.CENTER);
    menu.setVisible(false);
    settingsPanel.add(settingsButton, BorderLayout.EAST);
    // settingsPanel.add(soundButton, BorderLayout.EAST);

  }

  /** Creates the sidebar with the panels of the opponents. */
  private void createOpponentsPanel() {
    otherPlayerBoards = new ArrayList<>();
    boardsOfOpponentsPanel = new JPanel();
    boardsOfOpponentsPanel.setOpaque(false);
    Dimension opponentsPanelDimension =
        new Dimension((int) (frameDimension.width * 0.25), (int) (frameDimension.height * 0.94));
    boardsOfOpponentsPanel.setMaximumSize(opponentsPanelDimension);
    boardsOfOpponentsPanel.setPreferredSize(opponentsPanelDimension);
    boardsOfOpponentsPanel.setLayout(new GridLayout(3, 1));

    List<String> listOfActivePlayers = controller.getPlayerNamesList();
    Dimension playerBoardDimension = new Dimension(frameDimension.width - 20, 200);
    for (String opponentPlayer : listOfActivePlayers) {
      if (!opponentPlayer.equals(controller.getNickOfActivePlayer())) {
        PlayerBoard playerBoard =
            new SmallPlayerBoard(controller, null, opponentPlayer, playerBoardDimension);
        otherPlayerBoards.add(playerBoard);
        boardsOfOpponentsPanel.add(playerBoard);
      }
    }
    add(boardsOfOpponentsPanel, BorderLayout.WEST);
  }

  /** Used by view to update all widgets in Center Board. */
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

  /**
   * Updates not current player boards. Searches the index of the current player in the list of all
   * active players. Shows all the boards of players in this list after the current player and after
   * that before the current player. In such a way the order of players is preserved.
   */
  public void updateOtherPlayerBoards() {

    for (PlayerBoard othersPlayerBoard : otherPlayerBoards) {
      boardsOfOpponentsPanel.remove(othersPlayerBoard);
    }
    otherPlayerBoards.clear();
    List<String> listOfActivePlayers = controller.getPlayerNamesList();
    String nameOfCurrentPlayer = controller.getNickOfActivePlayer();
    int indexOfCurrentPlayer = listOfActivePlayers.indexOf(nameOfCurrentPlayer);
    Dimension playerBoardDimension = new Dimension(frameDimension.width - 20, 200);
    initializeOnePart(
        indexOfCurrentPlayer + 1,
        listOfActivePlayers.size(),
        listOfActivePlayers,
        playerBoardDimension);

    initializeOnePart(0, indexOfCurrentPlayer, listOfActivePlayers, playerBoardDimension);
  }

  private void initializeOnePart(
      int indexOfCurrentPlayer,
      int listOfActivePlayersSize,
      List<String> listOfActivePlayers,
      Dimension playerBoardDimension) {
    for (int i = indexOfCurrentPlayer; i < listOfActivePlayersSize; i++) {
      PlayerBoard playerBoard =
          new SmallPlayerBoard(controller, null, listOfActivePlayers.get(i), playerBoardDimension);
      boardsOfOpponentsPanel.add(playerBoard);
      otherPlayerBoards.add(playerBoard);
    }
  }
}
