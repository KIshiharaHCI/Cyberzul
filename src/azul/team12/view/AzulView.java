package azul.team12.view;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.view.board.GameBoard;
import azul.team12.view.listeners.TileClickListener;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AzulView extends JFrame {

  private static final long serialVersionUID = 7526472295622776147L;

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

  public AzulView(GameModel model, Controller controller) throws HeadlessException {
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
    hsmButton = new JButton("Hot Seat Mode");
    networkButton = new JButton("Network Mode");
    addPlayerButton = new JButton("+ Add Player");
    playButton = new JButton("Play");
    //Labels

  }

  private void addEventListeners() {
    //TODO: swap lambda expressions with controller functions
    // use the same listener for every Tile (source/destination) object
    TileClickListener tileClickListener = new TileClickListener();
    hsmButton.addActionListener(event -> showHSMCard());
    networkButton.addActionListener(event -> showNetworkCard());
    playButton.addActionListener(event -> showGameBoard(tileClickListener));

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

  /**
   * shows the GameBoard when Play Button is pressed.
   *
   * @param tileClickListener the listener that enables to check whether one has clicked on
   *                          a tile, the wall as a destination or the pattern lines as a
   *                          destination.
   */
  private void showGameBoard(TileClickListener tileClickListener) {
    JPanel gameBoardPanel = new JPanel();
    add(gameBoardPanel, GAMEBOARD_CARD);
    //GameBoard gameBoard = new GameBoard(gbl);
    GameBoard gameBoard = new GameBoard(4, tileClickListener);
    gameBoardPanel.add(gameBoard);
    gameBoard.repaint();
    showCard(GAMEBOARD_CARD);
  }

  private void showCard(String card) {
    layout.show(getContentPane(), card);
  }
}
