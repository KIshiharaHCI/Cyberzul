package azul.team12.view.board.playerBoard;

import java.awt.Dimension;
import javax.swing.JPanel;

public class TableCenterTiles extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  Tile tile;

  public TableCenterTiles(Tile tile) {
    this.tile = tile;
    setMinimumSize(new Dimension(600, 100));
//    createTableCenter();
  }
}
