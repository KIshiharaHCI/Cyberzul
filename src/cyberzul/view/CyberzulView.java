package cyberzul.view;

import cyberzul.controller.Controller;
import cyberzul.model.Model;
import cyberzul.model.events.*;
import cyberzul.network.client.messages.Message;
import cyberzul.network.client.messages.PlayerTextMessage;
import cyberzul.view.board.GameBoard;
import cyberzul.view.listeners.TileClickListener;
import cyberzul.view.board.ChatPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class CyberzulView extends JFrame implements PropertyChangeListener {

    private static final long serialVersionUID = 7526472295622776147L;

    private static final Logger LOGGER = LogManager.getLogger(CyberzulView.class);
    private static final String LOGIN_CARD = "login";
    private static final String HOT_SEAT_MODE_CARD = "hotseatmode";
    private static final String NETWORK_CARD = "networkmode";
    private static final String GAMEBOARD_CARD = "gameboard";
    private static final int FRAME_WIDTH = 1400;
    private static final int FRAME_HEIGHT = 800;
    private final Dimension frameDimension = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    private final double BACKGROUND_SCALE_FACTOR = 1;
    private final String BACKGROUND_PATH = "img/background.jpg";
    private CardLayout layout;
    private JTextField inputField;
    private JButton hotSeatModeButton;
    private JButton networkButton;
    private JButton addPlayerButton;
    private JButton playButton;
    private JButton testFourPlayersButton;
    private JButton testThreePlayersButton;
    private JButton testTwoPlayersButton;
    private JLabel numberOfLoggedInPlayersLabel, pleaseEnterNameLabel, selectModeLabel;
    private JLabel gameLogoLabel;
    private TileClickListener tileClickListener;
    private final transient Model model;
    private final transient Controller controller;
    private GameBoard gameBoard;

    /**
     * Create the Graphical User Interface of Azul.
     *
     * @param model      the model that handles the logic of the game.
     * @param controller the controller that is used to communicate with the view.
     * @throws HeadlessException
     */
    public CyberzulView(Model model, Controller controller) throws HeadlessException {
        this.setTitle("Cyberzul");
        this.model = model;
        this.controller = controller;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(frameDimension);
        setMaximumSize(frameDimension);
        setResizable(true);

        initializeWidgets();
        addEventListeners();
        createView();
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
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(resource));
        icon = new ImageIcon(icon.getImage().getScaledInstance(900, 150, Image.SCALE_SMOOTH));
        gameLogoLabel = new JLabel(icon);

        resource = getClass().getClassLoader().getResource("img/select-mode-banner.png");
        icon = new ImageIcon(new ImageIcon(
                Objects.requireNonNull(resource)).getImage().getScaledInstance(700, 40, Image.SCALE_SMOOTH));
        selectModeLabel.setIcon(icon);

        resource = getClass().getClassLoader().getResource("img/hotseat-button.png");
        icon = new ImageIcon(Objects.requireNonNull(resource));
        hotSeatModeButton.setIcon(icon);

        resource = getClass().getClassLoader().getResource("img/network-button.png");
        icon = new ImageIcon(Objects.requireNonNull(resource));
        networkButton.setIcon(icon);

        resource = getClass().getClassLoader().getResource("img/start-game-button.png");
        icon = new ImageIcon(Objects.requireNonNull(resource));
        playButton.setIcon(icon);

    }

    private void addEventListeners() {
        tileClickListener = new TileClickListener(controller, model);

        hotSeatModeButton.addActionListener(event -> {
            //the model should behave like a game model
            model.setStrategy(Model.GAME_MODEL);
            createHotSeatModeCard();
            showHSMCard();
        });
        networkButton.addActionListener(event -> {
            //the model should behave like a client model
            model.setStrategy(Model.CLIENT_MODEL);

            //TODO: ONLY TESTING. THE NEXT TO LINES CAN BE DELETED.
            createHotSeatModeCard();
            showHSMCard();
        });
        playButton.addActionListener(event -> {
            controller.startGame();
        });
        addPlayerButton.addActionListener(event -> {
                    controller.addPlayer(inputField.getText());
                    numberOfLoggedInPlayersLabel.setText(
                            "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
                    inputField.setText("");
                }
        );

        //TODO @Xue temporary test Buttons , need to delete
        testFourPlayersButton.addActionListener(event -> {
                    List<String> fourUserNameForTest = new ArrayList<>(
                            List.of("Iurri", "Kenji", "Marco", "Nils"));
                    for (String name : fourUserNameForTest) {
                        controller.addPlayer(name);
                    }
                    controller.startGame();
                }
        );
        testThreePlayersButton.addActionListener(event -> {
                    List<String> threeUserNameForTest = new ArrayList<>(List.of("Einen", "schoenen", "Tag"));
                    for (String name : threeUserNameForTest) {
                        controller.addPlayer(name);
                    }
                    controller.startGame();

                }
        );
        testTwoPlayersButton.addActionListener(event -> {
                    List<String> twoUserNameForTest = new ArrayList<>(List.of("Feier", "Abend"));
                    for (String name : twoUserNameForTest) {
                        controller.addPlayer(name);
                    }
                    controller.startGame();
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
                if (loginFailedEvent.getMessage().equals(LoginFailedEvent.NICKNAME_ALREADY_TAKEN)) {
                    showErrorMessage("Nickname is already taken.");
                } else if (loginFailedEvent.getMessage().equals(LoginFailedEvent.LOBBY_IS_FULL)) {
                    showErrorMessage("Lobby is full.");
                } else if (loginFailedEvent.getMessage().equals(LoginFailedEvent.ALREADY_LOGGED_IN)) {
                    showErrorMessage("Already logged in.");
                }
                //TODO - @ Nils add other events
            }
            case "GameStartedEvent" -> {
                addNewGameBoard(tileClickListener);
                showCard(GAMEBOARD_CARD);
                updateCenterBoard();
                updateRankingBoard();
                updateOtherPlayerBoards();
            }
            case "NextPlayersTurnEvent" -> {
                updateCenterBoard();
                updateOtherPlayerBoards();
                updateRankingBoard();
            }
            case "LoggedInEvent" -> {
                this.setTitle("Cyberzul - " + model.getPlayerName());
                numberOfLoggedInPlayersLabel.setText(
                        "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
                showErrorMessage("successfully logged in");

                }
            case ConnectedWithServerEvent.EVENT_NAME, UserJoinedEvent.EVENT_NAME ->
                    numberOfLoggedInPlayersLabel.setText(
                            "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
            case "RoundFinishedEvent" -> {
                updateCenterBoard();
                updateRankingBoard();
            }
            case "GameFinishedEvent" -> {
                updateCenterBoard();
                updateRankingBoard();
                GameFinishedEvent gameFinishedEvent = (GameFinishedEvent) customMadeGameEvent;
                showErrorMessage("User " + gameFinishedEvent.getWinner() + " won.");
            }
            case "IllegalTurnEvent" -> {
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
                showHSMCard();
            }
            case "NotYourTurnEvent" -> showErrorMessage(
                    "Please wait for other players so they can do their move");
            case "PlayerHasChosenTileEvent" -> {
                //TODO: FILL WITH FUNCTIONALITY
            }
            case "NoValidTurnToMakeEvent" -> showErrorMessage("No Valied Turn to make");
            case GameForfeitedEvent.EVENT_NAME -> {
                GameForfeitedEvent gameForfeitedEvent = (GameForfeitedEvent) customMadeGameEvent;
                showErrorMessage("Player " + gameForfeitedEvent.getForfeiter() +
                        " left the game and was replaced by an AI");
            }
            case "PlayerAddedMessageEvent" -> {
                PlayerAddedMessageEvent playerAddedMessageEvent = (PlayerAddedMessageEvent) customMadeGameEvent;
                ChatPanel.listModel.addElement(playerAddedMessageEvent.getMessage());
            }
            case "ChatMessageRemovedEvent" -> {
                ChatMessageRemovedEvent chatMessageRemovedEvent = (ChatMessageRemovedEvent) customMadeGameEvent;
                ChatPanel.listModel.removeElement(chatMessageRemovedEvent.getMessage());
                showErrorMessage("Only the last hundred messages are shown.");
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

    private void createView() {
        JPanel panel = new JPanel(layout);
        panel.setMinimumSize(frameDimension);
        panel.setMaximumSize(frameDimension);
        setContentPane(panel);

        JPanel login = new JPanel(new GridLayout(2, 1));
        login.setMinimumSize(frameDimension);
        login.setMaximumSize(frameDimension);
        login.add(gameLogoLabel);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(selectModeLabel);
        JPanel modeButtons = new JPanel();
        modeButtons.setOpaque(false);
        modeButtons.add(hotSeatModeButton);
        modeButtons.add(networkButton);
        container.add(modeButtons);
        container.setOpaque(false);

        login.add(container);

        JPanel backgroundPanel = new ImagePanel(login, BACKGROUND_PATH, FRAME_WIDTH, FRAME_HEIGHT,
                BACKGROUND_SCALE_FACTOR);
        add(backgroundPanel, LOGIN_CARD);

    }

    /**
     * Adds the Card for the preliminary inputs for the Hot Seat Mode to the card Layout of the view.
     * This card gets shown if the user selects "hot seat mode" at the start of the program.
     */
    private void createHotSeatModeCard() {
        JPanel hotSeatModePanel = new JPanel();
        setMinimumSize(frameDimension);
        setMaximumSize(frameDimension);
        inputField = new JTextField(10);
        numberOfLoggedInPlayersLabel =
                new JLabel("Number of Players: " + (model.getPlayerNamesList().size()) + ".");
        numberOfLoggedInPlayersLabel.setForeground(Color.WHITE);
        hotSeatModePanel.add(numberOfLoggedInPlayersLabel);
        hotSeatModePanel.add(pleaseEnterNameLabel);
        pleaseEnterNameLabel.setForeground(Color.WHITE);
        hotSeatModePanel.add(inputField);
        hotSeatModePanel.add(addPlayerButton);
        hotSeatModePanel.add(playButton);
        hotSeatModePanel.add(testFourPlayersButton);
        hotSeatModePanel.add(testThreePlayersButton);
        hotSeatModePanel.add(testTwoPlayersButton);
        JPanel backgroundPanel = new ImagePanel(hotSeatModePanel, BACKGROUND_PATH, FRAME_WIDTH,
                FRAME_HEIGHT, BACKGROUND_SCALE_FACTOR);
        add(backgroundPanel, HOT_SEAT_MODE_CARD);
    }

    private void createNetworkModeCard() {
        JPanel networkModePanel = new JPanel();
        setMinimumSize(frameDimension);
        setMaximumSize(frameDimension);
        inputField = new JTextField(10);
        numberOfLoggedInPlayersLabel =
                new JLabel("Number of Players: " + (model.getPlayerNamesList().size()) + ".");
        networkModePanel.add(numberOfLoggedInPlayersLabel);
        networkModePanel.add(pleaseEnterNameLabel);
        networkModePanel.add(inputField);
        networkModePanel.add(addPlayerButton);
        networkModePanel.add(playButton);
        JPanel backgroundPanel = new ImagePanel(networkModePanel, BACKGROUND_PATH, FRAME_WIDTH,
                FRAME_HEIGHT, BACKGROUND_SCALE_FACTOR);
        add(backgroundPanel, NETWORK_CARD);
    }


    private void showHSMCard() {
        showCard(HOT_SEAT_MODE_CARD);
    }

    private void showNetworkCard() {
        showCard(NETWORK_CARD);
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
        JPanel backgroundPanel = new ImagePanel(gameBoardPanel, BACKGROUND_PATH, FRAME_WIDTH,
                FRAME_WIDTH, BACKGROUND_SCALE_FACTOR);
        add(backgroundPanel, GAMEBOARD_CARD);
        gameBoard = new GameBoard(tileClickListener, controller, frameDimension);

        gameBoardPanel.add(gameBoard);
        gameBoard.repaint();
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
    }

}
