package azul.team12.view.board;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TileWithoutListener extends JPanel {

  private static final long serialVersionUID = 8L;
  private final int cell;
  private final int row;
  private final int size;

  public TileWithoutListener(int cell, int row, int size) {
    this.cell = cell;
    this.row = row;
    this.size = size;
    add(new JLabel(""));
    setPreferredSize(new Dimension(size, size));
    setMaximumSize(new Dimension(size, size));
    setMinimumSize(new Dimension(size, size));
    setBackground(Color.WHITE);

  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(size + 2, size + 2);
  }


}

