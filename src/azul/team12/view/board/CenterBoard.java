package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * create manufacturing plates with tiles and show the current player bord.
 */
public class CenterBoard extends JPanel {

  private final PlayerBoard currentPlayerBoard;

  Plates plates;

  private int numberOfPlates;

  public CenterBoard(int numberOfPlayers, TileClickListener tileClickListener) {
    setLayout(new BorderLayout());
    //setPreferredSize(new Dimension(1100, 800));
    numberOfPlates = numberOfPlayers * 2 + 1;
    plates = new Plates(numberOfPlates, tileClickListener);

    add(plates, BorderLayout.NORTH);

    TableCenterTiles tableCenter = new TableCenterTiles(tileClickListener);
    add(tableCenter, BorderLayout.CENTER);

    currentPlayerBoard = new PlayerBoard(40, tileClickListener);
    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));

    add(currentPlayerBoard, BorderLayout.SOUTH);

  }

}