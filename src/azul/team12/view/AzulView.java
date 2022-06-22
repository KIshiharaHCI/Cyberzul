package azul.team12.view;

import azul.team12.view.board.GameBoard;

import java.awt.*;
import javax.swing.*;

public class AzulView extends JFrame{

  private static final String LOGIN_CARD = "login";
  private static final String HSM_CARD = "hotseatmode";
  private static final String NETWORK_CARD = "networkmode";
  private static final String GAMEBOARD_CARD = "gameboard";
  private final GridBagLayout gbl;
  private CardLayout layout;
  private JTextArea inputNameArea;
  private JButton hsmButton;
  private JButton networkButton;
  private JButton addPlayerButton;
  private JButton playButton;
  private int numberOfPlayers;

  public AzulView() throws HeadlessException {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gbl = new GridBagLayout();
    //setLayout(new GridBagLayout());
    setSize (1200, 800);

    initializeWidgets();
    addEventListeners();
    createView();
  }

  private void initializeWidgets() {
    layout = new CardLayout();
    //Buttons
    hsmButton = new JButton("Hot Seat Mode");
    networkButton = new JButton("Network Mode");
    addPlayerButton = new JButton("+ Add Player");
    playButton = new JButton("Play");
    //Labels

  }

  private void addEventListeners() {
    //TODO: swap lambda expressions with controller functions
    hsmButton.addActionListener(event -> showHSMCard());
    networkButton.addActionListener(event -> showNetworkCard());
    playButton.addActionListener(event -> showGameBoard());

    /*
    addPlayerButton.addActionListener(
            //TODO: if numberOfPlayers < 5, add another inputNameArea, else disable button
    );
    */
  }

  private void createView() {
    JPanel panel = new JPanel(layout);
    setContentPane(panel);

    JPanel login = new JPanel();
    add(login, LOGIN_CARD);
    login.add(new JLabel("Select Mode"));
    login.add(hsmButton);
    login.add(networkButton);

    JPanel hsmPanel = new JPanel();
    inputNameArea = new JTextArea();
    add(hsmPanel, HSM_CARD);
    hsmPanel.add(new JLabel("Player 1: "));
    hsmPanel.add(inputNameArea);
    hsmPanel.add(addPlayerButton);
    hsmPanel.add(playButton);


    //controller.startGameBoard
  }

  //TODO: add propertyChange function
  private void showHSMCard() {
    showCard(HSM_CARD);
  }
  private void showNetworkCard() {
    showCard(NETWORK_CARD);
  }
  private void showGameBoard() {
    JPanel gameBoardPanel = new JPanel();
    add(gameBoardPanel, GAMEBOARD_CARD);
    GameBoard gameBoard = new GameBoard(gbl);
    gameBoardPanel.add(gameBoard);
    showCard(GAMEBOARD_CARD);
  }
  private void showCard(String card) {
    layout.show(getContentPane(), card);
  }

}
