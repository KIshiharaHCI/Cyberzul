package azul.team12.view.board.playerBoard;

import javax.swing.JButton;

public class PatternLinesStone extends JButton {
  private int row;
  private int column;

  public PatternLinesStone(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }
}
