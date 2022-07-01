package azul.team12.view.board;

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
  List<Plate> plateList;
  List<Tile> tileList;

  Plates plates;
  private Tile selectedTile = null;
  private Tile draggedTile = null;
  // TODO->lang of playerList
  private int numberOfPlates;

  /**
   * Creates the center board based on the number of players and with the tile click listeners.
   *
   * @param numberOfPlayers   the number of players
   * @param tileClickListener the tile click listener
   */
  public CenterBoard(int numberOfPlayers, TileClickListener tileClickListener) {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(1100, 800));

    numberOfPlates = numberOfPlayers * 2 + 1;
    plateList = new ArrayList<>();
    tileList = new ArrayList<>();
    plates = new Plates(numberOfPlates, this.plateList, this.tileList, tileClickListener);

    plates.setPreferredSize(new Dimension(1100, 130));
    add(plates);

    currentPlayerBoard = new PlayerBoard(40, tileClickListener);
    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));

    add(currentPlayerBoard, BorderLayout.SOUTH);

  }

}
