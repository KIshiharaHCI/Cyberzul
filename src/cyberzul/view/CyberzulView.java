package cyberzul.view;

import cyberzul.controller.Controller;
import cyberzul.model.CommonModel;
import cyberzul.model.Model;
import cyberzul.model.events.*;
import cyberzul.network.client.messages.GameStateMessage;
import cyberzul.network.server.Server;
import cyberzul.view.board.ChatPanel;
import cyberzul.view.board.GameBoard;
import cyberzul.view.board.MusicPlayerHelper;
import cyberzul.view.listeners.TileClickListener;
import cyberzul.view.panels.HotSeatLobbyScreen;
import cyberzul.view.panels.NetworkLobbyScreen;
import cyberzul.view.panels.SinglePlayerLobbyScreen;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * GUI for Cyberzul Changes its appearance based on the model information.
 */

public class CyberzulView extends JFrame implements PropertyChangeListener {
  @Serial
  private static final long serialVersionUID = 7526472295622776147L;

  private static final Logger LOGGER = LogManager.getLogger(CyberzulView.class);
  private static final String LOGIN_CARD = "login";
  private static final String hotSeatModeCard = "hotseatmode";
  private static final String NETWORK_CARD = "networkmode";
  private static final String SINGLEPLAYER_CARD = "singleplayermode";
  private static final String GAMEBOARD_CARD = "gameboard";
  private static final String GAMEOVER_CARD = "gameover";
  private static final int FRAME_WIDTH = 1400;
  private static final int FRAME_HEIGHT = 800;
  private static Font customFont;
  private final Dimension frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
  private final double backgroundScaleFactor = 1;
  private final String backgroundPath = "img/background.jpg";
  private final transient Model model;
  private final transient Controller controller;
  private final transient MusicPlayerHelper musicPlayerHelper;
  private String currentCard;
  private HotSeatLobbyScreen hotSeatLobbyScreen;
  private boolean hotseatMode = false;
  private NetworkLobbyScreen networkLobbyScreen;
  private SinglePlayerLobbyScreen singlePlayerPanel;
  private CardLayout layout;
  private JTextField inputField;
  private JButton hotSeatModeButton;
  private JButton networkButton;
  private JButton singlePlayerModeButton;
  private JButton addPlayerButton;
  private JButton playButton;
  private JButton testFourPlayersButton;
  private JButton testThreePlayersButton;
  private JButton testTwoPlayersButton;
  private JLabel numberOfLoggedInPlayersLabel;
  private JLabel pleaseEnterNameLabel;
  private JLabel selectModeLabel;
  private JLabel gameLogoLabel;
  private transient TileClickListener tileClickListener;
  private GameBoard gameBoard;
  //TODO: @Kenji feel free to change this. I needed it.
  private JButton joinServerButton;
  private JButton createServerButton;
  private transient BufferedImage gameOverImage;

  /**
   * Create the Graphical User Interface of Azul.
   *
   * @param model      the model that handles the logic of the game.
   * @param controller the controller that is used to communicate with the view.
   * @throws HeadlessException //TODO Kenji JavaDoc
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2"})
  public CyberzulView(final Model model, final Controller controller) throws HeadlessException {
    this.setTitle("Cyberzul");
    this.model = model;
    this.controller = controller;
    musicPlayerHelper = new MusicPlayerHelper();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);
    setResizable(false);

    initializeWidgets();

    initializeFont();
    addEventListeners();
    createView();
  }

  public static Font getCustomFont() {
    return customFont;
  }

  /**
   * Initializes the custom font used in the package for writing names etc.
   * Returned font is of default size
   */
  private void initializeFont() {
    try {
      customFont = Font.createFont(Font.TRUETYPE_FONT,
          new BufferedInputStream((requireNonNull(getClass().getClassLoader()
                  .getResourceAsStream("fonts/gameOfSquids.ttf")))));
      customFont = customFont.deriveFont(12f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(customFont);
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }
  }

  private void initializeWidgets() {
    layout = new CardLayout();
    //Buttons
    hotSeatModeButton = new JButton();
    hotSeatModeButton.setContentAreaFilled(false);
    hotSeatModeButton.setBorderPainted(false);
    networkButton = new JButton("Network Mode");
    networkButton.setContentAreaFilled(false);
    networkButton.setBorderPainted(false);
    singlePlayerModeButton = new JButton();
    singlePlayerModeButton.setContentAreaFilled(false);
    singlePlayerModeButton.setBorderPainted(false);
    addPlayerButton = new JButton("+ Add Player");
    playButton = new JButton();
    playButton.setContentAreaFilled(false);
    playButton.setBorderPainted(false);
    //temporary button to test the view
    testFourPlayersButton = new JButton("Test of 4 Players");
    testThreePlayersButton = new JButton("Test of 3 Players");
    testTwoPlayersButton = new JButton("Test of 2 Players");
    //Labels
    pleaseEnterNameLabel = new JLabel("Please enter your nickname:");
    selectModeLabel = new JLabel();
    //Icons
    URL resource = getClass().getClassLoader().getResource("img/gamelogo.png");
    ImageIcon icon = new ImageIcon(requireNonNull(resource));
    icon = new ImageIcon(icon.getImage().getScaledInstance(900, 150, Image.SCALE_SMOOTH));
    gameLogoLabel = new JLabel(icon);

    resource = getClass().getClassLoader().getResource("img/select-mode-banner.png");
    icon = new ImageIcon(new ImageIcon(
        requireNonNull(resource)).getImage()
        .getScaledInstance(700, 40, Image.SCALE_SMOOTH));
    selectModeLabel.setIcon(icon);

    resource = getClass().getClassLoader().getResource("img/hotseat-button.png");
    icon = new ImageIcon(requireNonNull(resource));
    hotSeatModeButton.setIcon(icon);

    resource = getClass().getClassLoader().getResource("img/network-button.png");
    icon = new ImageIcon(requireNonNull(resource));
    networkButton.setIcon(icon);

    resource = getClass().getClassLoader().getResource("img/singleplayer-button.png");
    icon = new ImageIcon(requireNonNull(resource));
    singlePlayerModeButton.setIcon(icon);

    resource = getClass().getClassLoader().getResource("img/start-game-button.png");
    icon = new ImageIcon(requireNonNull(resource));
    playButton.setIcon(icon);

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/game-over-background.jpg");
      gameOverImage = ImageIO.read(Objects.requireNonNull(imgUrl));
      gameOverImage.getScaledInstance(
          FRAME_WIDTH, FRAME_HEIGHT, Image.SCALE_SMOOTH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    //TODO: @Kenji kannst du gerne ändern, aber ich brauchte die Funktionalität jetzt
    joinServerButton = new JButton("JOIN CYBER SERVER");
    createServerButton = new JButton("CREATE NEW CYBER SERVER");
  }

  private void addEventListeners() {
    tileClickListener = new TileClickListener(controller, model);

    hotSeatModeButton.addActionListener(event -> {
      //the model should behave like a game model
      model.setGameModelStrategy();
      controller.setMode(CommonModel.HOT_SEAT_MODE);
      LOGGER.info("Mode was set to: " + controller.getMode());
      createHotSeatModeCard();
      showHsmCard();
    });
    networkButton.addActionListener(event -> {
      //the model should behave like a client model
      //TODO: ONLY TESTING. THE NEXT TO LINES CAN BE DELETED.
      createNetworkModeCard();
      showNetworkCard();
    });
    singlePlayerModeButton.addActionListener(event -> {
      //TODO - when play button is clicked on single player card --> first login user, then
      // use method single player mode --> probably via controller
      model.setGameModelStrategy();
      controller.setMode(CommonModel.SINGLE_PLAYER_MODE);
      createSinglePlayerModeCard();
      showSinglePlayerCard();
    });
    playButton.addActionListener(event -> {
      controller.startGame();
    });
    addPlayerButton.addActionListener(event -> {
          controller.addPlayer(
              inputField.getText());
          numberOfLoggedInPlayersLabel.setText(
              "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
          inputField.setText("");
        }
    );
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        handleModelUpdate(event);
      }
    });
  }

  /**
   * Handles the events that get fired from the model to the listeners of the model.
   *
   * @param event the event that was fired from the model.
   */
  private void handleModelUpdate(PropertyChangeEvent event) {
    Object customMadeGameEvent = event.getNewValue();

    String eventName = event.getPropertyName();
    LOGGER.info(eventName);

    switch (eventName) {
      case "LoginFailedEvent" -> {
        LoginFailedEvent loginFailedEvent = (LoginFailedEvent) customMadeGameEvent;
        showErrorMessage(loginFailedEvent.getMessage());
      }
      case "GameStartedEvent" -> {
        GameStartedEvent gameStartedEvent = (GameStartedEvent) customMadeGameEvent;
        ChatPanel.listModel.addElement(new GameStateMessage(gameStartedEvent.getChatMessage()));
        addNewGameBoard(tileClickListener);
        showCard(GAMEBOARD_CARD);
        updateCenterBoard();
        updateRankingBoard();
        updateOtherPlayerBoards();
        if (controller.getBulletMode()) {
          gameBoard.getTimer().start();
        }
      }
      case "NextPlayersTurnEvent" -> {
        NextPlayersTurnEvent nextPlayersTurnEvent = (NextPlayersTurnEvent) customMadeGameEvent;
        ChatPanel.listModel.addElement(new GameStateMessage(nextPlayersTurnEvent.getChatMessage()));
        if (controller.getBulletMode()) {
          gameBoard.getTimer().restart();
        }
        if (this.musicPlayerHelper.isPlayMusicOn()) {
          this.musicPlayerHelper.playTilePlacedMusic();
        }
        updateCenterBoard();
        updateOtherPlayerBoards();
        updateRankingBoard();
      }
      case "LoggedInEvent" -> {
        if (networkLobbyScreen != null) {
          networkLobbyScreen.updateinputField();
        }
        this.setTitle("Cyberzul - " + model.getPlayerName());
      }
      case ConnectedWithServerEvent.EVENT_NAME -> {
        networkLobbyScreen.updateUiAfterConnect();
      }
      case UserJoinedEvent.EVENT_NAME -> {
        if (networkLobbyScreen != null) {
          networkLobbyScreen.updateinputField();
        }
      }
      case "RoundFinishedEvent" -> {
        updateCenterBoard();
        updateRankingBoard();
      }
      case "GameFinishedEvent" -> {
        updateCenterBoard();
        updateRankingBoard();
        GameFinishedEvent gameFinishedEvent = (GameFinishedEvent) customMadeGameEvent;
        showNeutralMessage(gameFinishedEvent.getWinningMessage());
        gameBoard.getTimer().stop();
      }
      case "PlayerHas5TilesInARowEvent" -> {
        updateCenterBoard();
        updateRankingBoard();
        PlayerHas5TilesInArowEvent playerHas5TilesInArowEvent =
            (PlayerHas5TilesInArowEvent) customMadeGameEvent;
        //TODO - this error message should be shown in chat
        showErrorMessage("User " + playerHas5TilesInArowEvent.getEnder() + " ended the game.");
      }
      case "IllegalTurnEvent" -> {
        if (this.musicPlayerHelper.isPlayMusicOn()) {
          this.musicPlayerHelper.playIllegalTurnMusic();
        }
        IllegalTurnEvent illegalTurnEvent = new IllegalTurnEvent();
        showErrorMessage(illegalTurnEvent.getChatMessage());

      }
      case "GameNotStartableEvent" -> {
        GameNotStartableEvent gameNotStartableEvent = (GameNotStartableEvent) customMadeGameEvent;
        if (gameNotStartableEvent.getMessage().equals(GameNotStartableEvent.GAME_ALREADY_STARTED)) {
          showErrorMessage(GameNotStartableEvent.GAME_ALREADY_STARTED);
        } else if (gameNotStartableEvent.getMessage()
            .equals(GameNotStartableEvent.NOT_ENOUGH_PLAYER)) {
          showErrorMessage(GameNotStartableEvent.NOT_ENOUGH_PLAYER);
        }
      }
      case "GameCanceledEvent" -> {
        this.musicPlayerHelper.turnMusicOnOff(true);
        if (model.getMode() == CommonModel.NETWORK_MODE) {
          showNetworkCard();
        } else if (model.getMode() == CommonModel.HOT_SEAT_MODE) {
          showHsmCard();
        } else {
          showSinglePlayerCard();
        }
      }
      case "NotYourTurnEvent" -> {
        NotYourTurnEvent notYourTurnEvent = new NotYourTurnEvent();
        showErrorMessage(notYourTurnEvent.getChatMessage());
      }
      case "PlayerHasChosenTileEvent" -> {
        //TODO: FILL WITH FUNCTIONALITY
      }
      case "NoValidTurnToMakeEvent" -> showErrorMessage("No valid turn to make");
      //TODO: find better way to turn music off --> does not work as intended for hotseat mode.
      case GameForfeitedEvent.EVENT_NAME -> {
        //this.musicPlayerHelper.turnMusicOnOff(true);
        GameForfeitedEvent gameForfeitedEvent = (GameForfeitedEvent) customMadeGameEvent;
        System.err.println(model.getMode());
        System.err.println(gameForfeitedEvent.getForfeiter());
        System.err.println(model.getPlayerName());
        if (gameForfeitedEvent.getForfeiter().equals(model.getPlayerName())
                && (model.getMode() == CommonModel.NETWORK_MODE)) {
          showGameOverCard();
        } else {
          showErrorMessage("Player " + gameForfeitedEvent.getForfeiter()
              + " left the game and was replaced by an AI");
        }
      }
      case "PlayerAddedMessageEvent" -> {
        requireNonNull(ChatPanel.listModel);
        PlayerAddedMessageEvent playerAddedMessageEvent =
            (PlayerAddedMessageEvent) customMadeGameEvent;
        System.out.println(playerAddedMessageEvent.getMessage());
        ChatPanel.listModel.addElement(playerAddedMessageEvent.getMessage());
      }
      case "ChatMessageRemovedEvent" -> {
        ChatMessageRemovedEvent chatMessageRemovedEvent =
            (ChatMessageRemovedEvent) customMadeGameEvent;
        ChatPanel.listModel.removeElement(chatMessageRemovedEvent.getMessage());
        showErrorMessage("Only the last 10 messages are shown.");
      }
      case "PlayerJoinedChatEvent" -> {
        PlayerJoinedChatEvent playerJoinedChatEvent =
            (PlayerJoinedChatEvent) customMadeGameEvent;
        ChatPanel.listModel.addElement(playerJoinedChatEvent.getMessage());
      }
      case InvalidIpv4AddressEvent.EVENT_NAME -> {
        if (networkLobbyScreen != null) {
          networkLobbyScreen.illegalAddressEvent();
        }
      }
      case YouDisconnectedEvent.EVENT_NAME -> {
        showErrorMessage(
            "You got disconnected from the server.");
        showGameOverCard();
      }
      case BulletModeChangedEvent.EVENT_NAME -> {
        BulletModeChangedEvent bulletModeChangedEvent =
            (BulletModeChangedEvent) customMadeGameEvent;
        if (networkLobbyScreen != null) {
          networkLobbyScreen.updateBulletCheckBox(bulletModeChangedEvent.isBulletModeActivated());
        }
      }
      case ConnectionWithServerNotPossibleEvent.EVENT_NAME -> {
        if (networkLobbyScreen != null) {
          networkLobbyScreen.couldNotConnectServerMsg();
        }
      }
      default -> throw new AssertionError("Unknown event: " + eventName);
    }
  }

  /**
   * Show an error message as pop-up window to inform the user of an error.
   *
   * @param message the message with information about the error.
   */
  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Error!",
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Show a mesage as pop-up window informing the user of something neutral or positive.
   *
   * @param message the message with information about the event.
   */
  private void showNeutralMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "A new game event!",
        JOptionPane.INFORMATION_MESSAGE);
  }

  private void createView() {
    JPanel panel = new JPanel(layout);
    panel.setMinimumSize(frameDimension);
    panel.setMaximumSize(frameDimension);
    setContentPane(panel);

    JPanel login = new JPanel(null);
    login.setMinimumSize(frameDimension);
    login.setMaximumSize(frameDimension);

    singlePlayerModeButton.setBounds(235, 450, 200, 80);
    hotSeatModeButton.setBounds(590, 450, 200, 80);
    networkButton.setBounds(940, 450, 200, 80);
    login.add(hotSeatModeButton);
    login.add(networkButton);
    login.add(singlePlayerModeButton);


    JPanel backgroundPanel = new ImagePanel(login, "img/startbackground.jpg",
        FRAME_WIDTH, FRAME_HEIGHT,
        1);
    add(backgroundPanel, LOGIN_CARD);

    JPanel gameOverPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameOverImage, 0, 0, null);
      }
    };
    add(gameOverPanel, GAMEOVER_CARD);
  }

  /**
   * Adds the Card for the preliminary inputs for the Hot Seat Mode to the card Layout of the view.
   * This card gets shown if the user selects "hot seat mode" at the start of the program.
   */
  private void createHotSeatModeCard() {
    hotseatMode = true;
    hotSeatLobbyScreen = new HotSeatLobbyScreen(controller, frameDimension);
    JLayeredPane hotSeatModePanel = hotSeatLobbyScreen;
    JPanel backgroundPanel = new ImagePanel(hotSeatModePanel, backgroundPath, FRAME_WIDTH,
        FRAME_HEIGHT, backgroundScaleFactor);
    add(backgroundPanel, hotSeatModeCard);
  }

  private void createSinglePlayerModeCard() {
    JLayeredPane singlePlayerModePanel = new SinglePlayerLobbyScreen(controller, frameDimension);
    JPanel backgroundPanel = new ImagePanel(singlePlayerModePanel, backgroundPath, FRAME_WIDTH,
        FRAME_HEIGHT, backgroundScaleFactor);
    add(backgroundPanel, SINGLEPLAYER_CARD);

  }

  private void createNetworkModeCard() {
    networkLobbyScreen = new NetworkLobbyScreen(controller, model, frameDimension);
    JLayeredPane networkModePanel = networkLobbyScreen;
    //setMinimumSize(frameDimension);
    //setMaximumSize(frameDimension);
    //inputField = new JTextField(10);
    //numberOfLoggedInPlayersLabel =
    //new JLabel("Number of Players: 0.");
    ////JLayeredPane networkModePanel = new NetworkPanel(controller, frameDimension);
    //JPanel networkModePanel = new JPanel();
    //networkModePanel.add(numberOfLoggedInPlayersLabel);
    //networkModePanel.add(pleaseEnterNameLabel);
    //networkModePanel.add(inputField);
    //networkModePanel.add(addPlayerButton);
    //networkModePanel.add(playButton);
    JPanel backgroundPanel = new ImagePanel(networkModePanel, backgroundPath, FRAME_WIDTH,
        FRAME_HEIGHT, backgroundScaleFactor);
    add(backgroundPanel, NETWORK_CARD);

    //TODO: @Kenji das darfst du gerne ändern, aber ich brauchte die Funktionalität gerade.
    networkModePanel.add(createServerButton);
    networkModePanel.add(joinServerButton);
  }

  private void showHsmCard() {
    showCard(hotSeatModeCard);
  }

  private void showNetworkCard() {
    showCard(NETWORK_CARD);
  }

  private void showSinglePlayerCard() {
    showCard(SINGLEPLAYER_CARD);
  }

  /**
   * Shows the game over screen.
   */
  public void showGameOverCard() {
    showCard(GAMEOVER_CARD);
  }

  /**
   * shows the GameBoard when Play Button is pressed.
   *
   * @param tileClickListener the listener that enables to check whether one has clicked on a tile,
   *                          the wall as a destination or the pattern lines as a destination.
   */
  private void addNewGameBoard(TileClickListener tileClickListener) {
    JPanel gameBoardPanel = new JPanel();
    gameBoardPanel.setMinimumSize(frameDimension);
    gameBoardPanel.setMaximumSize(frameDimension);
    JPanel backgroundPanel = new ImagePanel(gameBoardPanel, backgroundPath, FRAME_WIDTH,
        FRAME_HEIGHT, backgroundScaleFactor);
    add(backgroundPanel, GAMEBOARD_CARD);
    String playerName;
    if (hotseatMode) {
      playerName = controller.getNickOfActivePlayer();
    } else {
      playerName = model.getPlayerName();
    }
    gameBoard = new GameBoard(this, tileClickListener, controller, frameDimension, playerName,
        hotseatMode, musicPlayerHelper);

    gameBoardPanel.add(gameBoard);
    gameBoard.repaint();
    this.musicPlayerHelper.init();
  }

  /**
   * Updates all Widgets in the Center Board for the new active player.
   */
  private void updateCenterBoard() {
    gameBoard.updateCenterBoard();
  }

  /**
   * Updates the game rankings.
   */
  private void updateRankingBoard() {
    gameBoard.updateRankingBoard();
  }

  private void updateOtherPlayerBoards() {
    gameBoard.updateOtherPlayerBoards();
  }

  /**
   * Used by EventListener to change the Panels being shown such as the Login panel, Gameboard
   * panel, etc.
   *
   * @param card the name of the panel to show.
   */
  private void showCard(String card) {
    layout.show(getContentPane(), card);
    currentCard = card;
  }

  @SuppressFBWarnings("DM_EXIT")
  @Override
  public void dispose() {
    super.dispose();
    if (model.isStrategyChosen()
        && model.getMode() == CommonModel.NETWORK_MODE) {
      controller.replacePlayerByAi(model.getPlayerName());
      model.removePropertyChangeListener(this);
    }
    if (Server.isRunning()) {
      Server.stop();
      System.exit(0);
    }

    //controller.dispose();
  }
}
