package azul.team12.view;

import azul.team12.controller.Controller;
import azul.team12.model.GameModel;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.view.board.GameBoard;
import azul.team12.view.listeners.TileClickListener;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AzulView extends JFrame implements PropertyChangeListener {

  private static final long serialVersionUID = 7526472295622776147L;

  private static final String LOGIN_CARD = "login";
  private static final String HOT_SEAT_MODE_CARD = "hotseatmode";
  private static final String NETWORK_CARD = "networkmode";
  private static final String GAMEBOARD_CARD = "gameboard";
  private final GridBagLayout gbl;
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


  private GameModel model;
  private Controller controller;
  private GameBoard gameBoard;

  /**
   * Create the Graphical User Interface of Azul.
   *
   * @param model      the model that handles the logic of the game.
   * @param controller the controller that is used to communicate with the view.
   * @throws HeadlessException
   */
  public AzulView(GameModel model, Controller controller) throws HeadlessException {
    this.model = model;
    this.controller = controller;
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gbl = new GridBagLayout();
    setMinimumSize(new Dimension(1400, 900));
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    initializeWidgets();
    addEventListeners();
    createView();
  }

  private void initializeWidgets() {
    layout = new CardLayout();
    //Buttons
    hotSeatModeButton = new JButton("Hot Seat Mode");
    networkButton = new JButton("Network Mode");
    addPlayerButton = new JButton("+ Add Player");
    playButton = new JButton("Play");
    //temporary button to test the view
    testFourPlayersButton = new JButton("Test of 4 Players");
    testThreePlayersButton = new JButton("Test of 3 Players");
    testTwoPlayersButton = new JButton("Test of 2 Players");
    //Labels
    pleaseEnterNameLabel = new JLabel("Please enter your nickname:");
    selectModeLabel = new JLabel("Select Mode");
  }

  private void addEventListeners() {
    TileClickListener tileClickListener = new TileClickListener(controller, model);
    hotSeatModeButton.addActionListener(event -> showHSMCard());
    networkButton.addActionListener(event -> showNetworkCard());
    playButton.addActionListener(event -> {
      controller.startGame();
      addNewGameBoard(tileClickListener);
      showCard(GAMEBOARD_CARD);
    });
    addPlayerButton.addActionListener(event -> {
          controller.addPlayer(inputField.getText());
          numberOfLoggedInPlayersLabel.setText(
              "Number of Players: " + (model.getPlayerNamesList().size()) + ".");
          inputField.setText("");
        }
    );
    testFourPlayersButton.addActionListener(event -> {
        List<String> fourUserNameForTest = new ArrayList<>(List.of("Iurri", "Kenji", "Marco", "Nils"));
        for(String name : fourUserNameForTest){
            controller.addPlayer(name);
        }
        controller.startGame();
        addNewGameBoard(tileClickListener);
        showCard(GAMEBOARD_CARD);

    }
    );
    testThreePlayersButton.addActionListener(event -> {
              List<String> threeUserNameForTest = new ArrayList<>(List.of("Einen", "schönen", "Tag"));
              for(String name : threeUserNameForTest){
                controller.addPlayer(name);
              }
              controller.startGame();
              addNewGameBoard(tileClickListener);
              showCard(GAMEBOARD_CARD);

            }
    );
    testTwoPlayersButton.addActionListener(event -> {
              List<String> twoUserNameForTest = new ArrayList<>(List.of("Feier", "Abend"));
              for (String name : twoUserNameForTest) {
                controller.addPlayer(name);

                controller.startGame();
                addNewGameBoard(tileClickListener);
                showCard(GAMEBOARD_CARD);

              }
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

    switch (eventName) {
      case "LoginFailedEvent" -> {
        LoginFailedEvent loginFailedEvent = (LoginFailedEvent) customMadeGameEvent;
        if (loginFailedEvent.getMessage().equals(LoginFailedEvent.NICKNAME_ALREADY_TAKEN)) {
          showErrorMessage("Nickname is already taken.");
        } else if (loginFailedEvent.getMessage().equals(LoginFailedEvent.LOBBY_IS_FULL)) {
          showErrorMessage("Lobby is full.");
        }
        //TODO - @ Nils add other events
      }
      case "NextPlayersTurnEvent" -> {
        System.out.println("NextPlayersTurnEvent triggered " + "(sout at AzulView 125.)");
        clearBoard();
      }
      //default -> throw new AssertionError("Unknown event");
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
    setContentPane(panel);

    JPanel login = new JPanel();
    add(login, LOGIN_CARD);
    login.add(selectModeLabel);
    login.add(hotSeatModeButton);
    login.add(networkButton);

    createHotSeatModeCard();
  }

  /**
   * Adds the Card for the preliminary inputs for the Hot Seat Mode to the card Layout of the view.
   * This card gets shown if the user selects "hot seat mode" at the start of the program.
   */
  private void createHotSeatModeCard() {
    JPanel hotSeatModePanel = new JPanel();
    inputField = new JTextField(10);
    add(hotSeatModePanel, HOT_SEAT_MODE_CARD);
    numberOfLoggedInPlayersLabel =
        new JLabel("Number of Players: " + (model.getPlayerNamesList().size()) + ".");
    hotSeatModePanel.add(numberOfLoggedInPlayersLabel);
    hotSeatModePanel.add(pleaseEnterNameLabel);
    hotSeatModePanel.add(inputField);
    hotSeatModePanel.add(addPlayerButton);
    hotSeatModePanel.add(playButton);
    hotSeatModePanel.add(testFourPlayersButton);
    hotSeatModePanel.add(testThreePlayersButton);
    hotSeatModePanel.add(testTwoPlayersButton);
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
   * @param tileClickListener the listener that enables to check whether one has clicked on
   *                          a tile, the wall as a destination or the pattern lines as a
   *                          destination.
   */
  private void addNewGameBoard(TileClickListener tileClickListener) {
    JPanel gameBoardPanel = new JPanel();
    add(gameBoardPanel, GAMEBOARD_CARD);
    int numberOfPlayers = controller.getPlayerNamesList().size();
    gameBoard = new GameBoard(numberOfPlayers, tileClickListener, controller);
    gameBoardPanel.add(gameBoard);
    gameBoard.repaint();
  }

  /**
   * Clears Pattern Lines, Wall and Labels of current player.
   */
  private void clearBoard() {
    gameBoard.disposeCurrentPlayerBoard();
  }
  private void showCard(String card) {
    layout.show(getContentPane(), card);
  }
}
