package azul.team12.view;

import azul.team12.view.board.GameBoard;
import azul.team12.view.listeners.TileClickListener;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

  public AzulView() throws HeadlessException {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gbl = new GridBagLayout();
    setMinimumSize(new Dimension(1200, 800));
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
    setLayout(new GridLayout(1, 2));
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

  private void showGameBoard(TileClickListener tileClickListener) {
    JPanel gameBoardPanel = new JPanel();

    gameBoardPanel.setLayout(new GridLayout(1, 1));
    URL imgURL1 = getClass().getClassLoader().getResource("img/background.jpg");
    ImageIcon icon1 = new ImageIcon(
        new ImageIcon(imgURL1).getImage()
            .getScaledInstance((int) Math.round(this.getWidth() * 1.5),
                (int) Math.round(this.getHeight() * 1.5), Image.SCALE_DEFAULT));
    JLabel background = new JLabel(icon1);
    background.setLayout(new GridLayout(1, 1));

    GameBoard gameBoard = new GameBoard(4, tileClickListener);
    background.setBorder(BorderFactory.createEmptyBorder(80, 160, 80, 160));
    background.add(gameBoard);
    gameBoardPanel.add(background);
    background.setOpaque(false);
    //gameBoard.repaint();
    add(gameBoardPanel, GAMEBOARD_CARD);
    showCard(GAMEBOARD_CARD);
  }

  private void showCard(String card) {
    layout.show(getContentPane(), card);
  }

}
