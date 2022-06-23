package azul.team12.view.board.playerBoard;

import javax.swing.JButton;

public class Tile extends JButton {

  private int row;
  private int column;

  public Tile(int row, int column) {
    this.row = row;
    this.column = column;
  }

}
