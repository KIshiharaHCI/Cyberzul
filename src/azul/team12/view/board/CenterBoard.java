package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

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
  private JPanel rightSideBarPanel;
  private JPanel rankingBoardPanel;
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
    setPlatesAndTableCenterPanel();
    createRankingBoardAndButtonsPanel();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

    add(boardAndPlatesAndTablePanel);
    add(rightSideBarPanel);

  }

  private void createRankingBoardAndButtonsPanel() {
    Dimension rightSideBarDimension = new Dimension(panelDimension.width, (int) (panelDimension.height * 0.3));
    rightSideBarPanel = new JPanel();
    setMaximumSize(rightSideBarDimension);
    setMinimumSize(rightSideBarDimension);
    //setPreferredSize(rightSideBarDimension);
    JPanel rankingBoardPanel = new JPanel();
    rankingBoard = new RankingBoard(controller);
    rankingBoardPanel.add(rankingBoard);
    rightSideBarPanel.add(rankingBoardPanel);
    setBackground(Color.blue.brighter());

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

    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
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
