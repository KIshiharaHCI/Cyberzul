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

  private transient TileClickListener tileClickListener;

  private static final long serialVersionUID = 5L;
  private Dimension panelDimension;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(Controller controller, TileClickListener tileClickListener, Dimension panelDimension) {
    this.controller = controller;

    this.tileClickListener = tileClickListener;
    this.panelDimension = panelDimension;

    computeCenterBoardSize();
    setProperties();
    setPlatesAndTableCenterPanel();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

  }

  /**
   * Calculates the relative Size based on the Dimensions of the Dimensions of GameBoard.
   */
  private void computeCenterBoardSize() {
    panelDimension = new Dimension(
            (int) (panelDimension.getHeight() * 0.94),
            (int) ((panelDimension.getWidth() * 0.5) * 0.7)
    );
    setMinimumSize(panelDimension);
    setMaximumSize(panelDimension);

  }
  private void setProperties() {
    setBackground(new Color(110, 150, 100));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
  }

  private void setPlatesAndTableCenterPanel() {
    platesAndTableCenterPanel = new JPanel();
    platesAndTableCenterPanel.setSize(new Dimension(
            (int) panelDimension.getWidth(),
            (int) (panelDimension.getHeight() * 0.45)
    ));
    add(platesAndTableCenterPanel);
  }

  /**
   * Used by Constructor and AzulView to create and add a new PlayerBoard panel.
   */
  void createNewPlayerBoard() {
    currentPlayerBoard =
        new ActivePlayerBoard(controller, tileClickListener, controller.getNickOfActivePlayer(),panelDimension);

    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    add(currentPlayerBoard);
  }

  /**
   * Used by Constructor and AzulView to create and add a new Plates panel.
   */
  void createNewPlatesPanel() {
    List<Offering> factoryDisplays = controller.getOfferings().subList(1,controller.getOfferings().size());
    platesPanel = new PlatesPanel(factoryDisplays, tileClickListener);
    platesPanel.setPreferredSize(new Dimension(
            (int) (panelDimension.getWidth() * 0.6),
            (int) (panelDimension.getHeight() * 0.4)
    ));
    platesAndTableCenterPanel.add(platesPanel);
  }

  /**
   * Used by Constructor and AzulView to create and add a new TableCenter panel.
   */
  void createNewTableCenter() {
    Dimension platesAndTableCenterPanelDimension = new Dimension(
            platesAndTableCenterPanel.getWidth(),
            platesAndTableCenterPanel.getHeight()
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
