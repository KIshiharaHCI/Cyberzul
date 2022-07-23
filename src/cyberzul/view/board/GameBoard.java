package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.IconButton;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
  private final transient MusicPlayerHelper musicPlayerHelper;
  private JPanel settingsPanel = new JPanel(null);
  private JPanel boardsOfOpponentsPanel;
  private RankingBoard rankingBoard;
  private transient List<PlayerBoard> otherPlayerBoards;
  private IconButton soundButton;
  private IconButton settingsButton;
  private JPanel menu;
  private JSlider musicSoundSlider;
  private JSlider systemSoundSlider;
  private IconButton forfeitButton;
  private IconButton cancelButton;
  private IconButton restartButton;
  private JPanel systemSoundPanel;
  private JPanel musicSoundPanel = new JPanel(new GridLayout(2, 1));
  private JLabel musicSoundLabel;
  private JLabel systemSoundLabel;
  private TurnCountDownTimer timer;

  private final String nickOfCenterBoardPlayer;

  private final boolean hotSeatMode;

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
      TileClickListener tileClickListener,
      Controller controller,
      Dimension frameDimension,
      String nickOfCenterBoardPlayer,
      boolean hotSeatMode,
      MusicPlayerHelper musicPlayerHelper) {

    this.controller = controller;
    this.frameDimension = frameDimension;
    this.nickOfCenterBoardPlayer = nickOfCenterBoardPlayer;
    this.hotSeatMode = hotSeatMode;
    setLayout(new BorderLayout());
    setOpaque(false);
    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);

    createOpponentsPanel();
    createChatAndRankingBoardAndSettingPanel();

    center =
        new CenterBoard(
            controller, tileClickListener, nickOfCenterBoardPlayer, hotSeatMode, frameDimension);
    add(center, BorderLayout.CENTER);
    this.musicPlayerHelper = musicPlayerHelper;
  }

  /** Creates all of the sidebar widgets and instantiates Chat, Settings and Ranking Board. */
  private void createChatAndRankingBoardAndSettingPanel() {

    JPanel chatAndRankingBoardAndSettingPanel = createEastPanel();
    createRankingBoardAndSettingPanel(chatAndRankingBoardAndSettingPanel);
    createTimer(chatAndRankingBoardAndSettingPanel);
    createChatPanel(chatAndRankingBoardAndSettingPanel);
  }

  private void createRankingBoardAndSettingPanel(JPanel chatAndRankingBoardAndSettingPanel) {
    JPanel rankingBoardAndSettingPanel = new JPanel();
    rankingBoardAndSettingPanel.setLayout(new GridLayout(1, 2));
    rankingBoardAndSettingPanel.setOpaque(false);

    JPanel rankingBoardPanel = new JPanel();
    rankingBoard = new RankingBoard(controller);
    rankingBoardPanel.add(rankingBoard);
    rankingBoardAndSettingPanel.add(rankingBoard);
    createSettingsPanel();
    rankingBoardAndSettingPanel.add(settingsPanel);

    chatAndRankingBoardAndSettingPanel.add(rankingBoardAndSettingPanel, BorderLayout.NORTH);
  }

  private void createChatPanel(JPanel chatAndRankingBoardAndSettingPanel) {
    ChatPanel chatPanel = new ChatPanel(controller);
    JPanel wrapperForChat = new JPanel();
    wrapperForChat.setLayout(new BoxLayout(wrapperForChat, BoxLayout.Y_AXIS));
    wrapperForChat.add(chatPanel);
    wrapperForChat.setOpaque(false);
    wrapperForChat.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
    chatAndRankingBoardAndSettingPanel.add(wrapperForChat, BorderLayout.SOUTH);
  }

  private void createTimer(JPanel chatAndRankingBoardAndSettingPanel) {
    if (controller.getBulletMode()) {
      JLabel timerLabel = new JLabel();
      timerLabel.setPreferredSize(new Dimension(200, 30));
      timerLabel.setHorizontalAlignment(JLabel.LEFT);
      timerLabel.setFont(new Font("Dialog", Font.BOLD, 25));
      timerLabel.setFont(this.getTimerFont());
      timerLabel.setForeground(Color.GREEN);
      timer =
              new TurnCountDownTimer(
                      1000,
                      e -> {
                        if (timer.getTimerValue() == 0) {
                          timer.setTimerValue(30);
                        }
                        timer.setTimerValue(timer.getTimerValue() - 1);
                        timerLabel.setText(secondsToTimer(timer.getTimerValue()));
                      });
      timerLabel.setText(secondsToTimer(timer.getTimerValue()));
      timer.setInitialDelay(0);
      chatAndRankingBoardAndSettingPanel.add(timerLabel, BorderLayout.CENTER);
    }

  }

  /**
   * {@link GameBoard} eastern part.
   *
   * @return the created eastern Panel.
   */
  private JPanel createEastPanel() {
    JPanel chatAndRankingBoardAndSettingPanel = new JPanel();
    chatAndRankingBoardAndSettingPanel.setLayout(new BorderLayout());
    chatAndRankingBoardAndSettingPanel.setOpaque(false);
    Dimension chatAndRankingBoardAndButtonsPanelDimension =
        new Dimension((int) (frameDimension.width * 0.26), (int) (frameDimension.height * 0.8));
    chatAndRankingBoardAndSettingPanel.setMinimumSize(chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.setMaximumSize(chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.setPreferredSize(
        chatAndRankingBoardAndButtonsPanelDimension);
    chatAndRankingBoardAndSettingPanel.revalidate();

    add(chatAndRankingBoardAndSettingPanel, BorderLayout.EAST);
    return chatAndRankingBoardAndSettingPanel;
  }

  private Font getTimerFont() {
    Font timerFont = new Font("TimesRoman", Font.BOLD, 20);
    try {
      timerFont =
          Font.createFont(
                  Font.TRUETYPE_FONT,
                  Objects.requireNonNull(
                      getClass()
                          .getClassLoader()
                          .getResourceAsStream("fonts/digital-display-font.ttf")))
              .deriveFont(60f);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
    return timerFont;
  }

  /**
   * Create text for Timer.
   *
   * @param seconds time left in seconds.
   * @return the text for Timer.
   */
  private String secondsToTimer(int seconds) {
    String finalTimerString = "00";

    if (seconds < 10) {
      finalTimerString = finalTimerString + ":0" + seconds;
    } else {
      finalTimerString = finalTimerString + ":" + seconds;
    }

    return finalTimerString;
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public TurnCountDownTimer getTimer() {
    return timer;
  }

  /** Initialise all buttons for settingPanel. */
  private void initializeSettingWidgets() {

    soundButton =
        new IconButton(
            soundButtonPath, 10, 80, (int) (iconButtonSize * 0.95), (int) (iconButtonSize * 0.95));
    soundButton.addActionListener(
        e -> {
          this.musicPlayerHelper.turnMusicOnOff(this.musicPlayerHelper.isPlayMusicOn());
          if (soundButton.getOpacity() == 1f) {
            this.soundButton.setOpacity(0.5f);
          } else {
            this.soundButton.setOpacity(1f);
          }
        });

    settingsButton =
        new IconButton(
            menuButtonPath, 10, 10, (int) (iconButtonSize * 0.95), (int) (iconButtonSize * 0.95));

    settingsButton.addActionListener(ae -> menu.setVisible(!menu.isVisible()));

    forfeitButton = new IconButton("img/forfeit-button.png", 0, 0, 100, 39);
    cancelButton = new IconButton("img/cancel-button.png", 0, 0, 100, 39);
    restartButton = new IconButton("img/restart-button.png", 0, 0, 100, 39);

    forfeitButton.addActionListener(
        event -> controller.replacePlayerByAi(controller.getNickOfActivePlayer()));
    cancelButton.addActionListener(event -> controller.cancelGameForAllPlayers());
    restartButton.addActionListener(event -> controller.restartGame());

    musicSoundSlider = new JSlider(-40, 6);
    musicSoundSlider.addChangeListener(
        e -> {
          if (musicSoundSlider.getValue() < -39) {
            musicPlayerHelper.getVolumeBackground().setValue(-80);
          } else {
            musicPlayerHelper.getVolumeBackground().setValue(musicSoundSlider.getValue());
          }
        });
    musicSoundSlider.setValue(-17);
    systemSoundSlider = new JSlider(-18, 6);
    systemSoundSlider.addChangeListener(
        e -> {
          if (systemSoundSlider.getValue() < -17) {
            musicPlayerHelper.getVolumeTilePlaced().setValue(-80);
            musicPlayerHelper.getVolumeIllegalTurn().setValue(-80);
          } else {
            musicPlayerHelper.getVolumeTilePlaced().setValue(systemSoundSlider.getValue());
            musicPlayerHelper.getVolumeIllegalTurn().setValue(systemSoundSlider.getValue());
          }
        });
    systemSoundSlider.setValue(-6);
    systemSoundLabel = new JLabel();
    musicSoundLabel = new JLabel();
  }

  private void createMenuPanel() {
    menu = new JPanel(new GridLayout(5, 1));
    menu.setPreferredSize(new Dimension(150, 260));
    menu.setOpaque(false);
    createMusicSoundPanel();
    createSystemSoundPanel();
    menu.add(systemSoundPanel);
    menu.add(musicSoundPanel);
    menu.add(forfeitButton);
    menu.add(cancelButton);
    menu.add(restartButton);
  }

  private void createSystemSoundPanel() {
    systemSoundPanel = new JPanel(new GridLayout(2, 1));
    systemSoundPanel.setOpaque(false);
    systemSoundPanel.setPreferredSize(new Dimension(150, 50));

    systemSoundSlider.setPreferredSize(new Dimension(130, 20));
    systemSoundSlider.setOpaque(false);
    systemSoundPanel.add(systemSoundSlider);
    setLabelProperties(systemSoundLabel, "system sound");
    systemSoundPanel.add(systemSoundLabel);
  }

  private void createMusicSoundPanel() {
    musicSoundPanel = new JPanel(new GridLayout(2, 1));
    musicSoundPanel.setOpaque(false);
    musicSoundPanel.setPreferredSize(new Dimension(150, 50));

    musicSoundSlider.setPreferredSize(new Dimension(130, 20));
    musicSoundSlider.setOpaque(false);
    musicSoundPanel.add(musicSoundSlider);
    setLabelProperties(musicSoundLabel, "music sound");
    musicSoundPanel.add(musicSoundLabel);
  }

  private void setLabelProperties(JLabel label, String text) {
    label.setPreferredSize(new Dimension(100, 20));
    label.setForeground(new Color(255, 255, 255, 255));
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setText(text);
  }

  /** adds the menu with default visibility set to false. */
  private void createSettingsPanel() {
    initializeSettingWidgets();
    settingsPanel = new JPanel(new BorderLayout());
    settingsPanel.setOpaque(false);
    createMenuPanel();
    menu.setVisible(false);
    JPanel roundButtonsPanel = new JPanel(null);
    roundButtonsPanel.setPreferredSize(new Dimension(60, 260));
    roundButtonsPanel.setOpaque(false);
    roundButtonsPanel.add(settingsButton);
    roundButtonsPanel.add(soundButton);
    settingsPanel.add(menu, BorderLayout.CENTER);
    settingsPanel.add(roundButtonsPanel, BorderLayout.EAST);
  }

  private String getCurrentPlayer() {
    String playerName = nickOfCenterBoardPlayer;
    if (hotSeatMode) {
      playerName = controller.getNickOfActivePlayer();
    }
    return playerName;
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
      if (!opponentPlayer.equals(getCurrentPlayer())) {
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
    String nameOfCurrentPlayer = getCurrentPlayer();
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
