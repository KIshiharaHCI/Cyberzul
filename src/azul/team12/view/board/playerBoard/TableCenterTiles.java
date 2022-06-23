package azul.team12.view.board.playerBoard;

import java.awt.Dimension;
import javax.swing.JPanel;

public class TableCenterTiles extends JPanel {
  Tile tile;

  public TableCenterTiles(Tile tile) {
    this.tile = tile;
    setMinimumSize(new Dimension(600, 100));
//    createTableCenter();
  }
}
