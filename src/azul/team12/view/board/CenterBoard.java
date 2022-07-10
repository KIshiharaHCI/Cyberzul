package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The center of the table ("Tischmitte").
 */
public class CenterBoard extends JPanel {

  private final int WIDTH = 1100;
  private final int HEIGHT = 780;
  private transient final Controller controller;
  PlatesPanel platesPanel;
  TableCenterPanel tableCenterPanel;
  private PlayerBoard currentPlayerBoard;
  private JPanel platesAndTableCenterPanel;
  private JPanel rightSideBarPanel;
  private transient TileClickListener tileClickListener;

  private static final long serialVersionUID = 5L;
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
    setPlatesAndTableCenterPanel();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

  }

  /**
   * Calculates the relative Size for "this", PlateAndTableCenterPanel and PlayerBoard based on the Dimensions of the GameBoard.
   */
  private void computePanelSizes() {
    System.out.println(panelDimension.height );
    panelDimension = new Dimension(
            (int) (panelDimension.height * 0.94),
            (int) (panelDimension.width * 0.5)
    );
    topPanelDimension = new Dimension(
            (int) (panelDimension.width * 0.87),
            (int) (panelDimension.height * 0.45));
    bottomPanelDimension = new Dimension(
            (int) (panelDimension.width * 0.87),
            (int) (panelDimension.height * 0.55));

    setMinimumSize(panelDimension);
    setMaximumSize(panelDimension);
  }
  private void setProperties() {
    setLayout(new BorderLayout());
    setAlignmentY(Component.LEFT_ALIGNMENT);
  }

  private void setPlatesAndTableCenterPanel() {
    platesAndTableCenterPanel = new JPanel();
    platesAndTableCenterPanel.setMinimumSize(topPanelDimension);
    platesAndTableCenterPanel.setMaximumSize(topPanelDimension);
    add(platesAndTableCenterPanel,BorderLayout.NORTH);
  }

  /**
   * Used by Constructor and AzulView to create and add a new PlayerBoard panel.
   */
  void createNewPlayerBoard() {
    currentPlayerBoard =
        new ActivePlayerBoard(controller, tileClickListener, controller.getNickOfActivePlayer(), bottomPanelDimension);

    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    add(currentPlayerBoard, BorderLayout.CENTER);
  }

  /**
   * Used by Constructor and AzulView to create and add a new Plates panel.
   */
  void createNewPlatesPanel() {
    List<Offering> factoryDisplays = controller.getOfferings().subList(1,controller.getOfferings().size());
    platesPanel = new PlatesPanel(factoryDisplays, tileClickListener);
    platesPanel.setPreferredSize(new Dimension(
            (int) (topPanelDimension.getWidth() * 0.6),
            (int) (topPanelDimension.getHeight() * 0.4)
    ));
    platesAndTableCenterPanel.add(platesPanel);
  }

  /**
   * Used by Constructor and AzulView to create and add a new TableCenter panel.
   */
  void createNewTableCenter() {
    Dimension platesAndTableCenterPanelDimension = new Dimension(
            (int) topPanelDimension.getWidth(),
            (int) topPanelDimension.getHeight()
    );
    tableCenterPanel = new TableCenterPanel(controller, tileClickListener, platesAndTableCenterPanelDimension);
    //platesPanel.setPreferredSize(new Dimension(1100, 10));
    platesAndTableCenterPanel.add(tableCenterPanel);
  }

  /**
   * Removes all Panels of the last player who ended their turn.
   */
  public void removeAllPanels() {
    remove(tableCenterPanel);
    remove(platesPanel);
    remove(currentPlayerBoard);
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
}
