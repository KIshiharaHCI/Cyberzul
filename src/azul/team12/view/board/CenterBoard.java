package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.model.TableCenter;
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
  private transient TableCenter tableCenter;
  private transient TileClickListener tileClickListener;

  private static final long serialVersionUID = 5L;
  private Dimension frameDimension;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(Controller controller, TileClickListener tileClickListener, Dimension frameDimension) {
    this.controller = controller;
    this.tableCenter = (TableCenter) controller.getOfferings().get(0);
    this.tileClickListener = tileClickListener;
    this.frameDimension = frameDimension;
    computeCenterBoardSize();
    setProperties();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

  }

  private void computeCenterBoardSize() {
    int height = (int) (frameDimension.getHeight() * 0.94);
    int width = (int) ((frameDimension.getWidth() * 0.5) * 0.7);
    frameDimension = new Dimension(width, height);
    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);

  }

  private void setProperties() {
    setBackground(new Color(110, 150, 100));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
  }


  /**
   * Used by Constructor and AzulView to create and add a new PlayerBoard panel.
   */
  void createNewPlayerBoard() {
    currentPlayerBoard =
        new ActivePlayerBoard(controller, tileClickListener, controller.getNickOfActivePlayer());

    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    add(currentPlayerBoard);
  }

  /**
   * Used by Constructor and AzulView to create and add a new Plates panel.
   */
  void createNewPlatesPanel() {
    List<Offering> factoryDisplays = controller.getOfferings().subList(1,controller.getOfferings().size());
    platesPanel = new PlatesPanel(factoryDisplays, tileClickListener);
    platesPanel.setPreferredSize(new Dimension(1100, 130));
    add(platesPanel);
  }

  /**
   * Used by Constructor and AzulView to create and add a new TableCenter panel.
   */
  void createNewTableCenter() {
    tableCenterPanel = new TableCenterPanel(tableCenter, tileClickListener);
    //platesPanel.setPreferredSize(new Dimension(1100, 10));
    add(tableCenterPanel);
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
