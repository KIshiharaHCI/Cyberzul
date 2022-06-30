package azul.team12.view;

import azul.team12.view.board.GameBoard;
import azul.team12.controller.Controller;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import javax.swing.*;

public class AzulView extends JFrame {

  private static final long serialVersionUID = 7526472295622776147L;
  private final Controller controller;
  private final int WIDTH_OF_WINDOW = 1200;
  private final int HEIGHT_OF_WINDOW = 800;

  private static final String LOGIN_CARD = "login";
  private static final String HSM_CARD = "hotseatmode";
  private static final String NETWORK_CARD = "networkmode";
  private static final String GAMEBOARD_CARD = "gameboard";
  private CardLayout layout;
  private JTextArea inputNameArea;
  private JTextArea playerTwoNameArea;
  private JButton hsmButton;
  private JButton networkButton;
  private JButton addPlayerButton;
  private JButton playButton;

  public AzulView(Controller controller) throws HeadlessException {
    this.controller = controller;

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize(new Dimension(WIDTH_OF_WINDOW, HEIGHT_OF_WINDOW));

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
    addPlayerButton.setEnabled(false);
    playButton = new JButton("Play");
    playButton.setEnabled(false);

    //Textfields
    inputNameArea = new JTextArea(1,20);
    inputNameArea.setLineWrap(true);
    inputNameArea.setWrapStyleWord(true);
    inputNameArea.setBorder(new JTextField().getBorder());

    playerTwoNameArea = new JTextArea(1,20);
    playerTwoNameArea.setLineWrap(true);
    playerTwoNameArea.setWrapStyleWord(true);
    playerTwoNameArea.setBorder(new JTextField().getBorder());

    //Labels

  }

  private void addEventListeners() {
    hsmButton.addActionListener(event -> showHSMCard());
    networkButton.addActionListener(event -> showNetworkCard());
    playButton.addActionListener(event -> showGameBoard());
    addTextAreaListener(inputNameArea);
    addTextAreaListener(playerTwoNameArea);
  }

  private void addTextAreaListener(JTextArea jTextArea) {
    jTextArea.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() != KeyEvent.VK_ENTER) {
          return;
        }
        event.consume();
        Objects.requireNonNull(jTextArea);

        String nick = jTextArea.getText();
        controller.addPlayer(nick);
        jTextArea.setText(nick);
        jTextArea.setEnabled(false);
        if (controller.getPlayerNamesList().size() > 1) {
          playButton.setEnabled(true);
        }
      }
    });
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
    add(hsmPanel, HSM_CARD);
    hsmPanel.add(new JLabel("Please enter usernames and hit ENTER"));
    hsmPanel.add(new JLabel("Player 1: "));
    hsmPanel.add(inputNameArea);
    hsmPanel.add(new JLabel("Player 2: "));
    hsmPanel.add(playerTwoNameArea);
    hsmPanel.add(addPlayerButton);
    hsmPanel.add(playButton);
    //controller.startGameBoard
  }

  //TODO: add AzulView extends PropertyChange
  // override dispose(), propertyChange() methods.
  private void showHSMCard() {
    showCard(HSM_CARD);
  }

  private void showNetworkCard() {
    showCard(NETWORK_CARD);
  }

  private void showGameBoard() {

    controller.startGame();

    JPanel gameBoardPanel = new JPanel();
    add(gameBoardPanel, GAMEBOARD_CARD);
    //GameBoard gameBoard = new GameBoard(gbl);
    GameBoard gameBoard = new GameBoard();

    gameBoardPanel.add(gameBoard);
    showCard(GAMEBOARD_CARD);
  }

  private void showCard(String card) {
    layout.show(getContentPane(), card);
  }

}
