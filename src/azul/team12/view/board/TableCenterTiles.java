package azul.team12.view.board;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 * The tiles on the center of the table ("Tischmitte") (Center Board class)
 */
public class TableCenterTiles extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  Tile tile;

  public TableCenterTiles(Tile tile) {
    this.tile = tile;
    setMinimumSize(new Dimension(600, 100));
    //TODO: create Table Center
//    createTableCenter();
  }
}
