package azul.team12.view.board.playerBoard;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile extends JButton {

  private static final long serialVersionUID = 7526472295622776147L;
  private int row;
  private int column;

  public Tile(int row, int column) {
    this.row = row;
    this.column = column;
  }

  

}
