package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * The center of the table ("Tischmitte").
 */
public class CenterBoard extends JPanel {

  private static final long serialVersionUID = 5L;
  private final int WIDTH = 1100;
  private final int HEIGHT = 780;
  private transient final Controller controller;
  PlatesPanel platesPanel;
  RankingBoard rankingBoard;
  TableCenterPanel tableCenterPanel;
  private PlayerBoard currentPlayerBoard;
  private JPanel platesAndTableCenterPanel;
  private JPanel boardAndPlatesAndTablePanel;
  private JPanel activeUserButtonsPanel;
  private JButton forfeitButton, cancelButton, restartButton;
  private transient TileClickListener tileClickListener;
  private Dimension panelDimension, topPanelDimension, bottomPanelDimension;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(Controller controller, TileClickListener tileClickListener, Dimension panelDimension) {
    this.controller = controller;

    this.tileClickListener = tileClickListener;
    this.panelDimension = panelDimension;

    computePanelSizes();
    setProperties();

    boardAndPlatesAndTablePanel = new JPanel(new BorderLayout());
    boardAndPlatesAndTablePanel.setOpaque(false);
    setPlatesAndTableCenterPanel();
    createActiveUserButtonsPanel();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

    add(boardAndPlatesAndTablePanel);
    add(activeUserButtonsPanel);

  }

  private void createActiveUserButtonsPanel() {
    Dimension rightSideBarDimension = new Dimension(panelDimension.width, (int) (panelDimension.height * 0.3));
    activeUserButtonsPanel = new JPanel();
    activeUserButtonsPanel.setAlignmentY(Box.BOTTOM_ALIGNMENT);
    activeUserButtonsPanel.setLayout(new BoxLayout(activeUserButtonsPanel, BoxLayout.Y_AXIS));
    activeUserButtonsPanel.setOpaque(false);

    setMaximumSize(rightSideBarDimension);
    setMinimumSize(rightSideBarDimension);

    //TODO: replace with Align Bottom for the buttons if it works
    activeUserButtonsPanel.add(Box.createVerticalStrut(300));

    initializeButtons();
    add(activeUserButtonsPanel);

  }

  private void initializeButtons() {
    forfeitButton = new JButton();
    setButtonProperties(forfeitButton, "img/forfeit-button.png");


    forfeitButton.addActionListener(event -> {
      controller.replaceActivePlayerByAI();
    });

    cancelButton = new JButton();
    setButtonProperties(cancelButton, "img/cancel-button.png");


    cancelButton.addActionListener(event -> {
      controller.cancelGameForAllPlayers();
    });

    restartButton = new JButton();
    setButtonProperties(restartButton, "img/restart-button.png");


    restartButton.addActionListener(event -> {
      controller.restartGame();
    });
  }

  /**
   * Loads the button image and sets it on the button and removes the background of the JButton.
   * @param button the JButton to decorate
   * @param path file path for the custom button image
   */
  private void setButtonProperties(JButton button, String path) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    ImageIcon img = new ImageIcon(new ImageIcon(imgURL1).getImage().getScaledInstance(140, 52, Image.SCALE_DEFAULT));
    button.setIcon(img);
    button.setBorder(null);
    button.setContentAreaFilled(false);

    activeUserButtonsPanel.add(Box.createVerticalStrut(40));
    activeUserButtonsPanel.add(button);
  }

  /**
   * Calculates the relative Size for "this", PlateAndTableCenterPanel and PlayerBoard based on the Dimensions of the GameBoard.
   */
  private void computePanelSizes() {
    System.out.println(panelDimension.height);
    panelDimension = new Dimension(
            (int) (panelDimension.width * 0.45),
            (int) (panelDimension.height * 0.94)
    );
    topPanelDimension = new Dimension(
            (int) (panelDimension.width * 0.8),
            (int) (panelDimension.height * 0.45));
    bottomPanelDimension = new Dimension(
            (int) (panelDimension.width * 0.8),
            (int) (panelDimension.height * 0.55));

  }
  private void setProperties() {
    //setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    setMinimumSize(panelDimension);
    setMaximumSize(panelDimension);
    setOpaque(false);
  }

  private void setPlatesAndTableCenterPanel() {
    platesAndTableCenterPanel = new JPanel(new BorderLayout());
    platesAndTableCenterPanel.setMinimumSize(topPanelDimension);
    platesAndTableCenterPanel.setMaximumSize(topPanelDimension);
    boardAndPlatesAndTablePanel.add(platesAndTableCenterPanel, BorderLayout.NORTH);
  }

  /**
   * Used by Constructor and AzulView to create and add a new PlayerBoard panel.
   */
  void createNewPlayerBoard() {
    currentPlayerBoard =
        new ActivePlayerBoard(controller, tileClickListener, controller.getNickOfActivePlayer(), bottomPanelDimension);

    boardAndPlatesAndTablePanel.add(currentPlayerBoard, BorderLayout.CENTER);
  }

  /**
   * Used by Constructor and AzulView to create and add a new Plates panel.
   */
  void createNewPlatesPanel() {
    List<Offering> factoryDisplays = controller.getOfferings()
        .subList(1, controller.getOfferings().size());
    platesPanel = new PlatesPanel(factoryDisplays, tileClickListener);
    platesPanel.setPreferredSize(new Dimension(
            (int) (topPanelDimension.width * 0.6), topPanelDimension.height));
    platesAndTableCenterPanel.add(platesPanel, BorderLayout.CENTER);
  }

  /**
   * Used by Constructor and AzulView to create and add a new TableCenter panel.
   */
  void createNewTableCenter() {
    tableCenterPanel = new TableCenterPanel(controller, tileClickListener, topPanelDimension);
    //platesPanel.setPreferredSize(new Dimension(1100, 10));
    platesAndTableCenterPanel.add(tableCenterPanel, BorderLayout.EAST);
  }

  /**
   * Removes all Panels of the last player who ended their turn.
   */
  public void removeAllPanels() {
    platesAndTableCenterPanel.remove(tableCenterPanel);
    platesAndTableCenterPanel.remove(platesPanel);
    boardAndPlatesAndTablePanel.remove(currentPlayerBoard);
  }

  /**
   * Updates the current state of the CenterBoard by clearing and adding updated instances of each
   * widget.
   */
  public void updateCenterBoard() {
    removeAllPanels();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();
  }

  public TableCenterPanel getTableCenterPanel() {
    return tableCenterPanel;
  }

  public void updateRankingBoard() {
    rankingBoard.updateRankingBoard();
    validate();
  }
}
