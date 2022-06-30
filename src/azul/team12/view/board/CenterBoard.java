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
 * create manufacturing plates with tiles and show the current player bord.
 *
 *
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

  public CenterBoard(int numberOfPlayers, TileClickListener tileClickListener) {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(1100, 800));

    numberOfPlates = numberOfPlayers * 2 + 1;
    plateList = new ArrayList<>();
    tileList = new ArrayList<>();
    plates = new Plates(numberOfPlates, this.plateList, this.tileList, tileClickListener);

    plates.setPreferredSize(new Dimension(1100, 130));
    add(plates);

//    addMouseListener(new MyMouseListener());
//    addMouseMotionListener(new MyMouseMotionListener());

//    Plates plates = new Plates(numberOfPlates);

    currentPlayerBoard = new PlayerBoard(40, tileClickListener);
    currentPlayerBoard.setBorder(BorderFactory.createLineBorder(Color.GREEN));

    add(currentPlayerBoard, BorderLayout.SOUTH);

  }

  /**
   @Override
   protected void paintComponent(Graphics graphics) {
   Graphics2D g = (Graphics2D) graphics;

   for (Plate plate : plates.getPlateList()) {
   URL imgURL = getClass().getClassLoader().getResource("img/manufacturing-plate.png");
   ImageIcon icon = new ImageIcon(imgURL);

   g.drawImage(icon.getImage(),
   plate.getCenterX() - plate.getRadius(),
   plate.getCenterY() - plate.getRadius(),
   plate.getRadius() * 2, plate.getRadius() * 2, null);
   List<Tile> tiles = plate.getTileList();
   for (Tile tile : tiles) {
   if (tile.getImage() == null) {
   g.setColor(Color.WHITE);
   g.fillRect(0, 0, tile.getCellSize(),
   tile.getCellSize());
   }

   if (tile.getImage() != null) {
   //Draw image at its natural size of 125x125.
   g.drawImage(tile.getImage(), tile.getLeftUpperCornerX(),
   tile.getLeftUpperCornerY(),
   tile.getCellSize(), tile.getCellSize(), null);
   }

   //Add a border, red if picture currently has focus
   if (isFocusOwner()) {
   g.setColor(Color.RED);
   System.out.println("red");
   } else {
   g.setColor(Color.GREEN);
   System.out.println("green");
   }
   g.drawRect(tile.getLeftUpperCornerX(), tile.getLeftUpperCornerY(),
   tile.getCellSize(),
   tile.getCellSize()
   );

   }
   }

   }
   */
}
