package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The center of the table ("Tischmitte").
 */
public class CenterBoard extends JPanel {

  private final PlayerBoard currentPlayerBoard;
  private final Controller controller;
  List<Tile> tileList;

  PlatesPanel platesPanel;
  private Tile selectedTile = null;
  private Tile draggedTile = null;
  // TODO->lang of playerList
  private int numberOfPlates;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(Controller controller, TileClickListener tileClickListener, List<Offering> factoryDisplays) {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(1100, 800));

    this.controller = controller;
    tileList = new ArrayList<>();
    platesPanel = new PlatesPanel(factoryDisplays, this.tileList, tileClickListener);

    platesPanel.setPreferredSize(new Dimension(1100, 130));
    add(platesPanel);

    currentPlayerBoard = new PlayerBoard(controller,40, tileClickListener);
    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));

    add(currentPlayerBoard, BorderLayout.SOUTH);

  }

}
