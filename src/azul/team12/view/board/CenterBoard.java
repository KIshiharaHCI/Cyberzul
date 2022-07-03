package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.model.TableCenter;
import azul.team12.view.listeners.TileClickListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The center of the table ("Tischmitte").
 */
public class CenterBoard extends JPanel {

  private PlayerBoard currentPlayerBoard;
  private final Controller controller;
  private TableCenter tableCenter;
  private TileClickListener tileClickListener;
  List<Tile> tileList;

  PlatesPanel platesPanel;
  TableCenterPanel tableCenterPanel;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(Controller controller, TileClickListener tileClickListener) {
    this.controller = controller;
    this.tableCenter = (TableCenter) controller.getTableCenter();
    this.tileClickListener = tileClickListener;

    setLayout(new GridLayout(3,1));
    setPreferredSize(new Dimension(1100, 800));

    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();

  }


  /**
   * Used by Constructor and AzulView to create and add a new PlayerBoard panel.
   */
  void createNewPlayerBoard() {
    currentPlayerBoard = new PlayerBoard(controller,40, tileClickListener);

    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    add(currentPlayerBoard);
  }

  /**
   * Used by Constructor and AzulView to create and add a new Plates panel.
   */
  void createNewPlatesPanel() {
    List<Offering> factoryDisplays = controller.getFactoryDisplays();
    platesPanel = new PlatesPanel(factoryDisplays, tileClickListener);
    platesPanel.setPreferredSize(new Dimension(1100, 130));
    add(platesPanel);
  }
  /**
   * Used by Constructor and AzulView to create and add a new TableCenter panel.
   */
  void createNewTableCenter() {
    tableCenterPanel = new TableCenterPanel(tableCenter,tileClickListener);
    platesPanel.setPreferredSize(new Dimension(1100,10));
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
   * Updates the current state of the CenterBoard by clearing and adding updated instances of each widget.
   */
  public void updateCenterBoard() {
    removeAllPanels();
    createNewPlatesPanel();
    createNewTableCenter();
    createNewPlayerBoard();
  }
}
