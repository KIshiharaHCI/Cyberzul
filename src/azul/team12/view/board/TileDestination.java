package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TileDestination extends JPanel {

  private final int cell;
  private final int row;

  private final int size;

  public TileDestination(int cell, int row, int size, TileClickListener tileClickListener) {
    this.cell = cell;
    this.row = row;
    this.size = size;
    add(new JLabel("icon"));
    setPreferredSize(new Dimension(size, size));
    setMaximumSize(new Dimension(size, size));
    setMinimumSize(new Dimension(size, size));
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createLineBorder(Color.BLACK));

    addMouseListener(tileClickListener);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(size + 2, size + 2);
  }


}
