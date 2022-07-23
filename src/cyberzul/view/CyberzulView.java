package cyberzul.view;

import static java.util.Objects.requireNonNull;

import cyberzul.controller.Controller;
import cyberzul.model.Model;
import cyberzul.model.events.ChatMessageRemovedEvent;
import cyberzul.model.events.ConnectedWithServerEvent;
import cyberzul.model.events.GameFinishedEvent;
import cyberzul.model.events.GameForfeitedEvent;
import cyberzul.model.events.GameNotStartableEvent;
import cyberzul.model.events.InvalidIpv4AddressEvent;
import cyberzul.model.events.LoginFailedEvent;
import cyberzul.model.events.PlayerAddedMessageEvent;
import cyberzul.model.events.PlayerHasEndedTheGameEvent;
import cyberzul.model.events.PlayerJoinedChatEvent;
import cyberzul.model.events.UserJoinedEvent;
import cyberzul.model.events.YouConnectedEvent;
import cyberzul.model.events.YouDisconnectedEvent;
import cyberzul.network.server.Server;
import cyberzul.view.board.ChatPanel;
import cyberzul.view.board.GameBoard;
import cyberzul.view.board.MusicPlayerHelper;
import cyberzul.view.listeners.TileClickListener;
import cyberzul.view.panels.HotSeatLobbyScreen;
import cyberzul.view.panels.NetworkLobbyScreen;
import cyberzul.view.panels.SinglePlayerPanel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  private static final int FRAME_WIDTH = 1400;
  private static final int FRAME_HEIGHT = 800;
  private static Font customFont;
  private final Dimension frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
  private final double backgroundScaleFactor = 1;
  private final String backgroundPath = "img/background.jpg";
  private final transient Model model;
  private final transient Controller controller;
  private final transient MusicPlayerHelper musicPlayerHelper;
  private String CURRENT_CARD;
  private HotSeatLobbyScreen hotSeatLobbyScreen;
  private boolean hotseatMode = false;
  private NetworkLobbyScreen networkLobbyScreen;
  private SinglePlayerPanel singlePlayerPanel;
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
    setResizable(true);

    initializeWidgets();
    initializeFont();
    addEventListeners();
    createView();
  }

  //  @Override
  //  public void dispose() {
  //    this.musicPlayerHelper.stopBackgroundMusic();
  //    this.musicPlayerHelper.closeAllOfMusicPlayer();
  //    this.gameBoard.getTimer.stop();
  //    super.dispose();
  //  }

  public static Font getCustomFont() {
    return customFont;
  }

  /**
   * Initializes the custom font used in the package for writing names etc.
   * Returned font is of default size
   */
  private void initializeFont() {
    try {
      //create the font to use.
      customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Game Of Squids.otf"));
      customFont = customFont.deriveFont(12f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      //register the font
      //IMPORTANT: call .deriveFont(size) when not using default font size 12f
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


    //TODO: @Kenji kannst du gerne ändern, aber ich brauchte die Funktionalität jetzt
    joinServerButton = new JButton("JOIN CYBER SERVER");
    createServerButton = new JButton("CREATE NEW CYBER SERVER");
  }

  private void addEventListeners() {
    tileClickListener = new TileClickListener(controller, model);

    hotSeatModeButton.addActionListener(event -> {
      //the model should behave like a game model
      model.setGameModelStrategy();
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
      //TODO: setstrategy
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

    //TODO: @Kenji feel free to change this. I needed it.
    createServerButton.addActionListener(event -> {
      String ipAddress = Server.start();
      model.setClientModelStrategy(ipAddress);
      JOptionPane.showMessageDialog(null, "IP Address of the cyber "
          + "server: " + ipAddress, "IP Address", 1);
    });

    joinServerButton.addActionListener(event -> {
      String ipAddress = JOptionPane.showInputDialog(
          "Please enter the IP Address of the cyber server you want to join.");
      model.setClientModelStrategy(ipAddress);
    });


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
        addNewGameBoard(tileClickListener);
        showCard(GAMEBOARD_CARD);
        updateCenterBoard();
        updateRankingBoard();
        updateOtherPlayerBoards();
        gameBoard.getTimer().start();
      }
      case "NextPlayersTurnEvent" -> {
        gameBoard.getTimer().restart();
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
        numberOfLoggedInPlayersLabel.setText(
            "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
        showNeutralMessage("successfully logged in");
      }
      case ConnectedWithServerEvent.EVENT_NAME -> {
        numberOfLoggedInPlayersLabel.setText(
                "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
      }
      case UserJoinedEvent.EVENT_NAME -> {
        if (networkLobbyScreen != null)
          networkLobbyScreen.updateinputField();
      }
      case "RoundFinishedEvent" -> {
        updateCenterBoard();
        updateRankingBoard();
      }
      case "GameFinishedEvent" -> {
        updateCenterBoard();
        updateRankingBoard();
        GameFinishedEvent gameFinishedEvent = (GameFinishedEvent) customMadeGameEvent;
        showErrorMessage(gameFinishedEvent.getWinningMessage());
        gameBoard.getTimer().stop();
      }
      case "PlayerHasEndedTheGameEvent" -> {
        updateCenterBoard();
        updateRankingBoard();
        PlayerHasEndedTheGameEvent playerHasEndedTheGameEvent =
            (PlayerHasEndedTheGameEvent) customMadeGameEvent;
        //TODO - this error message should be shown in chat
        showErrorMessage("User " + playerHasEndedTheGameEvent.getEnder() + " ended the game.");
      }
      case "IllegalTurnEvent" -> {
        if (this.musicPlayerHelper.isPlayMusicOn()) {
          this.musicPlayerHelper.playIllegalTurnMusic();
        }
        showErrorMessage("Illegal turn.");
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
        showHsmCard();
      }
      case "NotYourTurnEvent" -> showErrorMessage(
          "Please wait for other players so they can do their move");
      case "PlayerHasChosenTileEvent" -> {
        //TODO: FILL WITH FUNCTIONALITY
      }
      case "NoValidTurnToMakeEvent" -> showErrorMessage("No valid turn to make");
      case GameForfeitedEvent.EVENT_NAME -> {
        GameForfeitedEvent gameForfeitedEvent = (GameForfeitedEvent) customMadeGameEvent;
        showErrorMessage("Player " + gameForfeitedEvent.getForfeiter()
            + " left the game and was replaced by an AI");
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
        showErrorMessage("Only the last hundred messages are shown.");
      }
      case "PlayerJoinedChatEvent" -> {
        PlayerJoinedChatEvent playerJoinedChatEvent =
            (PlayerJoinedChatEvent) customMadeGameEvent;
        ChatPanel.listModel.addElement(playerJoinedChatEvent.getMessage());
      }
      case InvalidIpv4AddressEvent.EVENT_NAME -> {
        showErrorMessage("The provided String can't be parsed into a valid IPv4 address.");
      }
      case YouConnectedEvent.EVENT_NAME -> {
        networkLobbyScreen.updateUiAfterConnect();
        showNeutralMessage("You connected to the server.");
      }
      case YouDisconnectedEvent.EVENT_NAME -> showErrorMessage(
          "You got disconnected from the server.");
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
    JPanel singlePlayerModePanel = new SinglePlayerPanel(frameDimension);
    JPanel backgroundPanel = new ImagePanel(singlePlayerModePanel, backgroundPath, FRAME_WIDTH,
        FRAME_HEIGHT, backgroundScaleFactor);
    add(backgroundPanel, SINGLEPLAYER_CARD);

  }

  private void createNetworkModeCard() {
    networkLobbyScreen = new NetworkLobbyScreen(controller, model, frameDimension);
    JLayeredPane networkModePanel = networkLobbyScreen;
//    setMinimumSize(frameDimension);
//    setMaximumSize(frameDimension);
//    inputField = new JTextField(10);
//    numberOfLoggedInPlayersLabel =
//        new JLabel("Number of Players: 0.");
//    //JLayeredPane networkModePanel = new NetworkPanel(controller, frameDimension);
//    JPanel networkModePanel = new JPanel();
//    networkModePanel.add(numberOfLoggedInPlayersLabel);
//    networkModePanel.add(pleaseEnterNameLabel);
//    networkModePanel.add(inputField);
//    networkModePanel.add(addPlayerButton);
//    networkModePanel.add(playButton);
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
    gameBoard = new GameBoard(tileClickListener, controller, frameDimension, playerName,
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
    CURRENT_CARD = card;
  }
}
